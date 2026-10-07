package com.mobilkontrol.tracking

import com.mobilkontrol.receiver.AdminReceiver
import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context

/**
 * İleri Ebeveyn Kontrol Modu (yalnızca Device Owner olarak ayarlanmış cihazlarda çalışır).
 * Kurulum: cihaz fabrika ayarlarına sıfırlanıp ADB ile
 *   adb shell dpm set-device-owner com.mobilkontrol/.receiver.AdminReceiver
 * komutu gerekir. Standart modda bu sınıf güvenli şekilde devre dışı kalır.
 */
class AdvancedModeManager(private val context: Context) {

    private val dpm = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    private val admin = ComponentName(context, AdminReceiver::class.java)

    fun isDeviceOwner(): Boolean = dpm.isDeviceOwnerApp(context.packageName)

    fun setLockTaskPackages(packages: Array<String>) {
        if (isDeviceOwner()) {
            dpm.setLockTaskPackages(admin, packages)
        }
    }

    fun lockTaskNow(activity: Activity) {
        if (isDeviceOwner()) {
            try { activity.startLockTask() } catch (_: Exception) { }
        }
    }
}
