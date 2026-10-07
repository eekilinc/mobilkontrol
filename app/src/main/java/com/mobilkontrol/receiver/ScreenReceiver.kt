package com.mobilkontrol.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mobilkontrol.service.ScreenTrackingService

class ScreenReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Servis zaten tick'te ekran durumunu okur; burada servisi tazelemek için tetikliyoruz.
        context.startForegroundService(Intent(context, ScreenTrackingService::class.java))
    }
}
