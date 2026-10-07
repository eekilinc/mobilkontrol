package com.mobilkontrol

import androidx.room.Room
import android.content.Context
import com.mobilkontrol.data.local.AppDatabase
import com.mobilkontrol.data.local.DailyUsageEntity
import com.mobilkontrol.data.local.UsageHistoryEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class AppDatabaseTest {

    private lateinit var db: AppDatabase

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            androidx.test.core.app.ApplicationProvider.getApplicationContext<Context>(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun tearDown() = db.close()

    @Test
    fun upsertAndReadDailyUsage() = runBlocking {
        db.dao().upsertDailyUsage(DailyUsageEntity("2026-10-07", 43, 15, false))
        val entity = db.dao().getDailyUsage("2026-10-07")
        assertEquals(43, entity?.usedMinutes)
        assertEquals(15, entity?.bonusMinutes)
    }

    @Test
    fun historyQueryReturnsSortedWeek() = runBlocking {
        db.dao().upsertHistory(UsageHistoryEntity("2026-10-01", 48, 60))
        db.dao().upsertHistory(UsageHistoryEntity("2026-10-02", 60, 60))
        db.dao().upsertHistory(UsageHistoryEntity("2026-10-03", 35, 60))
        val list = db.dao().observeLast7().first()
        assertEquals(3, list.size)
        assertEquals("2026-10-03", list.first().date) // DESC: en yeni ilk
    }
}
