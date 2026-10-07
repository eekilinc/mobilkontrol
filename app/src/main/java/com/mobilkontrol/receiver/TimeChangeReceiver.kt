package com.mobilkontrol.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mobilkontrol.service.ScreenTrackingService

class TimeChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        context.startForegroundService(Intent(context, ScreenTrackingService::class.java))
    }
}
