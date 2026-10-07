package com.mobilkontrol

import android.app.Application
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.mobilkontrol.work.UsageCheckWorker
import java.util.concurrent.TimeUnit

class MobilKontrolApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            val request = PeriodicWorkRequestBuilder<UsageCheckWorker>(15, TimeUnit.MINUTES).build()
            WorkManager.getInstance(this)
                .enqueueUniquePeriodicWork(
                    "usage_check",
                    androidx.work.ExistingPeriodicWorkPolicy.KEEP,
                    request
                )
        } catch (_: Exception) {
            // Test ortamında WorkManager init edilemeyebilir.
        }
    }
}
