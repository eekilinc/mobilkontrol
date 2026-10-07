package com.mobilkontrol.tracking

import android.app.AppOpsManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Process
import android.provider.Settings

class UsageStatsTracker(private val context: Context) {

    fun hasPermission(): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.unsafeCheckOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(),
            context.packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }

    /**
     * [sinceMs] ile şimdi arasındaki toplam ön plan kullanımını (dakika) döndürür.
     */
    fun usedMinutesSince(sinceMs: Long): Long {
        if (!hasPermission()) return 0L
        val usm = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val until = System.currentTimeMillis()
        val stats = usm.queryAndAggregateUsageStats(sinceMs, until)
        var total = 0L
        for ((pkg, stat) in stats) {
            if (pkg == context.packageName) continue // kendimizi saymıyoruz
            total += stat.totalTimeInForeground
        }
        return total / 60_000L
    }

    /** Günün başlangıcından itibaren en çok kullanılan uygulamalar (paket adı, dakika). */
    fun topAppsToday(limit: Int = 5): List<Pair<String, Long>> {
        if (!hasPermission()) return emptyList()
        val usm = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val since = System.currentTimeMillis() - 24 * 3600_000L
        val stats = usm.queryAndAggregateUsageStats(since, System.currentTimeMillis())
        return stats.values
            .filter { it.totalTimeInForeground > 60_000L }
            .sortedByDescending { it.totalTimeInForeground }
            .take(limit)
            .map { it.packageName to (it.totalTimeInForeground / 60_000L) }
    }

    companion object {
        fun usageAccessIntent() = android.content.Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
    }
}
