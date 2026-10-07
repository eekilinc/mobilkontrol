package com.mobilkontrol.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import com.mobilkontrol.data.repository.ParentalRepository
import com.mobilkontrol.ui.LockActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ScreenTrackingService : Service() {

    private val scope = CoroutineScope(Dispatchers.Default + Job())
    private lateinit var repository: ParentalRepository
    private var lastTickWall: Long = 0L
    private var lastTickElapsed: Long = 0L
    private var lastUsedQueryWall: Long = 0L
    private lateinit var usageTracker: com.mobilkontrol.tracking.UsageStatsTracker
    private val screenReceiver = com.mobilkontrol.receiver.ScreenReceiver()
    private var remoteServer: com.mobilkontrol.remote.RemoteControlServer? = null

    override fun onCreate() {
        super.onCreate()
        repository = ParentalRepository(applicationContext)
        usageTracker = com.mobilkontrol.tracking.UsageStatsTracker(applicationContext)
        createChannel()
        registerReceiver(screenReceiver, android.content.IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_SCREEN_OFF)
            addAction(Intent.ACTION_USER_PRESENT)
        })
        scope.launch {
            repository.settings.remoteOn.collect { enabled ->
                if (enabled && remoteServer == null) {
                    remoteServer = com.mobilkontrol.remote.RemoteControlServer(repository).also { it.start() }
                } else if (!enabled) {
                    remoteServer?.stop()
                    remoteServer = null
                }
            }
        }
        val notification = buildNotification("Kullanım süresi takip ediliyor")
        if (Build.VERSION.SDK_INT >= 29) {
            startForeground(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
        } else {
            startForeground(1, notification)
        }
        lastTickWall = System.currentTimeMillis()
        lastTickElapsed = SystemClock.elapsedRealtime()
        scope.launch { loop() }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        try { unregisterReceiver(screenReceiver) } catch (_: Exception) { }
        scope.launch { repository.settings.setScreenOn(false) }
    }

    private suspend fun loop() {
        while (true) {
            try {
                tick()
            } catch (_: Exception) { }
            delay(30_000)
        }
    }

    suspend fun tick() {
        val today = repository.ensureToday()
        val now = System.currentTimeMillis()
        val elapsed = SystemClock.elapsedRealtime()

        val wallDelta = now - lastTickWall
        val elapsedDelta = elapsed - lastTickElapsed

        // Tarih/saat manipülasyonu kontrolü: 60 saniyelik bir tick'te wall saati
        // elapsed saatinden belirgin şekilde farklı ilerleyemez.
        val suspicious = wallDelta < 0 || (wallDelta - elapsedDelta > 10_000) || (elapsedDelta - wallDelta > 10_000)
        if (suspicious) {
            lastTickWall = now
            lastTickElapsed = elapsed
            lastUsedQueryWall = now
            repository.settings.setAnchors(now, elapsed)
            return
        }

        // Kullanım sorgusu bir önceki tick'in wall zamanından yapılır.
        val usageQueryStart = if (lastUsedQueryWall == 0L) now - elapsedDelta else lastUsedQueryWall
        lastUsedQueryWall = now
        lastTickElapsed = elapsed
        lastTickWall = now

        val manualLock = repository.settings.getManualLock()
        val frozen = repository.settings.getFrozen()
        val remaining0 = remainingMinutes(today)

        if (frozen || manualLock) {
            GateState.locked = manualLock || remaining0 <= 0
            maybeLock(manualLock || remaining0 <= 0)
            return
        }

        if (isScreenOn()) {
            // Öncelik: UsageStatsManager ile gerçek uygulama kullanım süresi.
            // İzin yoksa ekran açık süresi fallback olarak kullanılır.
            val minutesToAdd = if (usageTracker.hasPermission()) {
                usageTracker.usedMinutesSince(usageQueryStart)
            } else {
                elapsedDelta / 60_000
            }
            if (minutesToAdd > 0) repository.addUsedMinutes(minutesToAdd.toInt())
        }
        repository.settings.setScreenOn(isScreenOn())

        val updated = repository.getTodayUsage()
        val remaining = remainingMinutes(updated)
        checkWarnings(remaining)
        GateState.locked = manualLock || remaining <= 0
        if (remaining <= 0) maybeLock(true)
    }

    private suspend fun remainingMinutes(today: com.mobilkontrol.data.local.DailyUsageEntity): Int {
        val limit = repository.settings.getDailyLimitNow()
        return (limit + today.bonusMinutes) - today.usedMinutes
    }

    private fun isScreenOn(): Boolean {
        val pm = getSystemService(PowerManager::class.java)
        return pm?.isInteractive == true
    }

    private suspend fun checkWarnings(remaining: Int) {
        val nm = getSystemService(NotificationManager::class.java)
        fun notify(id: Int, text: String) {
            val n = NotificationCompat.Builder(this@ScreenTrackingService, WARN_CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setContentTitle("MobilKontrol")
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()
            nm.notify(id, n)
        }
        val on15 = repository.settings.warn15On.first()
        val on5 = repository.settings.warn5On.first()
        val on1 = repository.settings.warn1On.first()
        if (on15 && remaining <= 15 && !repository.settings.wasWarned15()) {
            repository.settings.setWarned15(true)
            notify(2, "Bugünkü kullanım sürenizin bitmesine 15 dakika kaldı.")
        }
        if (on5 && remaining <= 5 && !repository.settings.wasWarned5()) {
            repository.settings.setWarned5(true)
            notify(3, "Kullanım sürenizin bitmesine 5 dakika kaldı.")
        }
        if (on1 && remaining <= 1 && !repository.settings.wasWarned1()) {
            repository.settings.setWarned1(true)
            notify(4, "Kullanım sürenizin bitmesine 1 dakika kaldı.")
        }
    }

    private fun maybeLock(lock: Boolean) {
        if (lock) {
            val intent = Intent(this, LockActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            startActivity(intent)
        }
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Ekran Süresi Takibi",
                NotificationManager.IMPORTANCE_LOW
            )
            val channel2 = NotificationChannel(
                WARN_CHANNEL_ID,
                "Uyarılar",
                NotificationManager.IMPORTANCE_HIGH
            )
            val nm = getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(channel)
            nm.createNotificationChannel(channel2)
        }
    }

    private fun buildNotification(text: String): Notification =
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("MobilKontrol")
            .setContentText(text)
            .setOngoing(true)
            .build()

    companion object {
        const val CHANNEL_ID = "tracking"
        const val WARN_CHANNEL_ID = "warnings"
    }
}
