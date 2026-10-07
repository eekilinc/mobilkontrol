package com.mobilkontrol.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mobilkontrol.service.ScreenTrackingService
import android.content.Intent

class UsageCheckWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        applicationContext.startForegroundService(Intent(applicationContext, ScreenTrackingService::class.java))
        return Result.success()
    }
}
