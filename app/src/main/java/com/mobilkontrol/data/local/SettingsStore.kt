package com.mobilkontrol.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "parental_settings")

class SettingsStore(private val context: Context) {

    companion object {
        val KEY_PIN_HASH = stringPreferencesKey("pin_hash")
        val KEY_DAILY_LIMIT = intPreferencesKey("daily_limit_minutes")
        val KEY_SETUP_DONE = booleanPreferencesKey("setup_done")
        val KEY_FROZEN = booleanPreferencesKey("frozen")
        val KEY_MANUAL_LOCK = booleanPreferencesKey("manual_lock")
        val KEY_LAST_DATE = stringPreferencesKey("last_date")
        val KEY_ANCHOR_WALL = longPreferencesKey("anchor_wall_ms")
        val KEY_ANCHOR_ELAPSED = longPreferencesKey("anchor_elapsed_ms")
        val KEY_WARNED_15 = booleanPreferencesKey("warned_15")
        val KEY_WARNED_5 = booleanPreferencesKey("warned_5")
        val KEY_WARNED_1 = booleanPreferencesKey("warned_1")
        val KEY_SCREEN_ON = booleanPreferencesKey("screen_on")
        val KEY_WARN15_ON = booleanPreferencesKey("warn15_on")
        val KEY_WARN5_ON = booleanPreferencesKey("warn5_on")
        val KEY_WARN1_ON = booleanPreferencesKey("warn1_on")
        val KEY_REMOTE_ON = booleanPreferencesKey("remote_on")
    }

    val pinHash: Flow<String?> = context.dataStore.data.map { it[KEY_PIN_HASH] }
    val dailyLimit: Flow<Int> = context.dataStore.data.map { it[KEY_DAILY_LIMIT] ?: 60 }
    val setupDone: Flow<Boolean> = context.dataStore.data.map { it[KEY_SETUP_DONE] ?: false }
    val frozen: Flow<Boolean> = context.dataStore.data.map { it[KEY_FROZEN] ?: false }
    val manualLock: Flow<Boolean> = context.dataStore.data.map { it[KEY_MANUAL_LOCK] ?: false }
    val lastDate: Flow<String?> = context.dataStore.data.map { it[KEY_LAST_DATE] }
    val screenOn: Flow<Boolean> = context.dataStore.data.map { it[KEY_SCREEN_ON] ?: false }

    private suspend fun <T> read(key: Preferences.Key<T>): T? = context.dataStore.data.map { it[key] }.first()

    suspend fun setPinHash(hash: String) { context.dataStore.edit { it[KEY_PIN_HASH] = hash } }
    suspend fun setDailyLimit(minutes: Int) { context.dataStore.edit { it[KEY_DAILY_LIMIT] = minutes } }
    suspend fun setSetupDone(value: Boolean) { context.dataStore.edit { it[KEY_SETUP_DONE] = value } }
    suspend fun setFrozen(value: Boolean) { context.dataStore.edit { it[KEY_FROZEN] = value } }
    suspend fun setManualLock(value: Boolean) { context.dataStore.edit { it[KEY_MANUAL_LOCK] = value } }
    suspend fun setLastDate(date: String) { context.dataStore.edit { it[KEY_LAST_DATE] = date } }
    suspend fun setAnchors(wall: Long, elapsed: Long) {
        context.dataStore.edit {
            it[KEY_ANCHOR_WALL] = wall
            it[KEY_ANCHOR_ELAPSED] = elapsed
        }
    }
    suspend fun setScreenOn(value: Boolean) { context.dataStore.edit { it[KEY_SCREEN_ON] = value } }
    suspend fun setWarned15(v: Boolean) { context.dataStore.edit { it[KEY_WARNED_15] = v } }
    suspend fun setWarned5(v: Boolean) { context.dataStore.edit { it[KEY_WARNED_5] = v } }
    suspend fun setWarned1(v: Boolean) { context.dataStore.edit { it[KEY_WARNED_1] = v } }

    suspend fun wasWarned15(): Boolean = read(KEY_WARNED_15) ?: false
    suspend fun wasWarned5(): Boolean = read(KEY_WARNED_5) ?: false
    suspend fun wasWarned1(): Boolean = read(KEY_WARNED_1) ?: false
    suspend fun getManualLock(): Boolean = read(KEY_MANUAL_LOCK) ?: false
    suspend fun getFrozen(): Boolean = read(KEY_FROZEN) ?: false
    suspend fun getDailyLimitNow(): Int = read(KEY_DAILY_LIMIT) ?: 60
    suspend fun getScreenOnNow(): Boolean = read(KEY_SCREEN_ON) ?: false
    suspend fun getLastDateNow(): String? = read(KEY_LAST_DATE)
    suspend fun getAnchorWall(): Long? = read(KEY_ANCHOR_WALL)
    suspend fun getAnchorElapsed(): Long? = read(KEY_ANCHOR_ELAPSED)

    val warn15On: Flow<Boolean> = context.dataStore.data.map { it[KEY_WARN15_ON] ?: true }
    val warn5On: Flow<Boolean> = context.dataStore.data.map { it[KEY_WARN5_ON] ?: true }
    val warn1On: Flow<Boolean> = context.dataStore.data.map { it[KEY_WARN1_ON] ?: true }
    val remoteOn: Flow<Boolean> = context.dataStore.data.map { it[KEY_REMOTE_ON] ?: false }

    suspend fun setWarn15On(v: Boolean) { context.dataStore.edit { it[KEY_WARN15_ON] = v } }
    suspend fun setWarn5On(v: Boolean) { context.dataStore.edit { it[KEY_WARN5_ON] = v } }
    suspend fun setWarn1On(v: Boolean) { context.dataStore.edit { it[KEY_WARN1_ON] = v } }
    suspend fun setRemoteOn(v: Boolean) { context.dataStore.edit { it[KEY_REMOTE_ON] = v } }
}
