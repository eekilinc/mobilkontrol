package com.mobilkontrol.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.mobilkontrol.R
import com.mobilkontrol.data.repository.ParentalRepository
import com.mobilkontrol.ui.MainActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class RemainingWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val repo = ParentalRepository(context)
        val (used, bonus, limit) = runBlocking {
            val usage = repo.getTodayUsage()
            val l = repo.settings.dailyLimit.first()
            Triple(usage.usedMinutes, usage.bonusMinutes, l)
        }
        val remaining = (limit + bonus - used).coerceAtLeast(0)

        for (id in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_remaining)
            views.setTextViewText(R.id.widget_remaining_text, "$remaining dk kaldı")
            val intent = Intent(context, MainActivity::class.java)
            val pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            views.setOnClickPendingIntent(R.id.widget_remaining_text, pi)
            appWidgetManager.updateAppWidget(id, views)
        }
    }
}
