package com.mobilkontrol.data.repository

import android.content.Context
import com.mobilkontrol.data.local.AppDatabase
import com.mobilkontrol.data.local.DailyUsageEntity
import com.mobilkontrol.data.local.SettingsStore
import com.mobilkontrol.data.local.UsageHistoryEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ParentalRepository(context: Context) {
    val settings = SettingsStore(context)
    private val dao = AppDatabase.get(context).dao()

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    fun today(): String = LocalDate.now().format(formatter)

    suspend fun ensureToday(): DailyUsageEntity {
        val t = today()
        val lastDate = settings.getLastDateNow()
        if (lastDate != t) {
            val yesterday = dao.getDailyUsage(lastDate ?: "")
            if (yesterday != null) {
                dao.upsertHistory(
                    UsageHistoryEntity(
                        date = yesterday.date,
                        totalUsageMinutes = yesterday.usedMinutes,
                        dailyLimitMinutes = settings.getDailyLimitNow()
                    )
                )
            }
            dao.upsertDailyUsage(DailyUsageEntity(date = t, usedMinutes = 0, bonusMinutes = 0, manualLock = false))
            settings.setLastDate(t)
            settings.setFrozen(false)
            settings.setManualLock(false)
            settings.setWarned15(false)
            settings.setWarned5(false)
            settings.setWarned1(false)
        }
        return dao.getDailyUsage(t) ?: DailyUsageEntity(t, 0).also { dao.upsertDailyUsage(it) }
    }

    suspend fun getTodayUsage(): DailyUsageEntity = ensureToday()

    fun observeToday(): Flow<DailyUsageEntity?> {
        return dao.observeDailyUsage(today())
    }

    suspend fun addUsedMinutes(minutes: Int) {
        val current = ensureToday()
        dao.upsertDailyUsage(current.copy(usedMinutes = current.usedMinutes + minutes))
    }

    suspend fun addBonusMinutes(minutes: Int) {
        val current = ensureToday()
        dao.upsertDailyUsage(current.copy(bonusMinutes = current.bonusMinutes + minutes))
    }

    suspend fun setManualLock(value: Boolean) {
        settings.setManualLock(value)
        val current = ensureToday()
        dao.upsertDailyUsage(current.copy(manualLock = value))
    }

    fun observeHistoryLast7(): Flow<List<UsageHistoryEntity>> = dao.observeLast7()
}
