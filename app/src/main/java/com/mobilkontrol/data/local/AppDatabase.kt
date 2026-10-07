package com.mobilkontrol.data.local

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "daily_usage")
data class DailyUsageEntity(
    @PrimaryKey val date: String,
    val usedMinutes: Int,
    val bonusMinutes: Int = 0,
    val manualLock: Boolean = false
)

@Entity(tableName = "usage_history")
data class UsageHistoryEntity(
    @PrimaryKey val date: String,
    val totalUsageMinutes: Int,
    val dailyLimitMinutes: Int
)

@Dao
interface AppDao {
    @Query("SELECT * FROM daily_usage WHERE date = :date LIMIT 1")
    suspend fun getDailyUsage(date: String): DailyUsageEntity?

    @Query("SELECT * FROM daily_usage WHERE date = :date LIMIT 1")
    fun observeDailyUsage(date: String): Flow<DailyUsageEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDailyUsage(entity: DailyUsageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertHistory(entity: UsageHistoryEntity)

    @Query("SELECT * FROM usage_history WHERE date >= :start ORDER BY date ASC")
    fun observeHistory(start: String): Flow<List<UsageHistoryEntity>>

    @Query("SELECT * FROM usage_history ORDER BY date DESC LIMIT 7")
    fun observeLast7(): Flow<List<UsageHistoryEntity>>
}
