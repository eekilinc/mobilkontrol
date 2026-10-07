package com.mobilkontrol.remote

/**
 * İleride eklenecek uzaktan kontrol özellikleri için mimari sözleşmesi.
 * İlk sürümde internetsiz çalışır; bu arayüz yalnızca yerel uygulama durumunu
 * "uzaktan komut alındı" gibi ele alır ve ileride REST/WebSocket ile değiştirilebilir.
 */
interface RemoteControlClient {
    suspend fun getUsageReport(date: String): UsageReport
    suspend fun addBonusMinutes(minutes: Int)
    suspend fun setLocked(locked: Boolean)
}

data class UsageReport(
    val date: String,
    val usedMinutes: Int,
    val bonusMinutes: Int,
    val dailyLimitMinutes: Int
)

class LocalRemoteControlClient : RemoteControlClient {
    override suspend fun getUsageReport(date: String): UsageReport =
        UsageReport(date, 0, 0, 60)

    override suspend fun addBonusMinutes(minutes: Int) { /* yerel: SettingsStore'a bağlanacak */ }
    override suspend fun setLocked(locked: Boolean) { /* yerel: SettingsStore'a bağlanacak */ }
}
