package com.mobilkontrol.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent

/**
 * Süre dolduğunda veya manuel kilit varken diğer uygulamaların açılmasını engeller.
 * Standart modda opsiyoneldir; kullanıcı Ayarlar ekranından etkinleştirebilir.
 */
class AppGateService : AccessibilityService() {

    private fun allowed(pkg: String): Boolean {
        if (pkg == "com.mobilkontrol" || pkg.startsWith("com.mobilkontrol.")) return true
        if (pkg == "android" || pkg.startsWith("android.")) return true
        if (pkg.startsWith("com.android.systemui")) return true
        if (pkg.startsWith("com.android.permissioncontroller")) return true
        if (pkg == "com.android.settings") return true
        // Başlatıcıyı (launcher) ve çeviriciyi engelleme.
        try {
            val pm = packageManager
            val home = Intent(Intent.ACTION_MAIN).apply { addCategory(Intent.CATEGORY_HOME) }
            val launchers = pm.queryIntentActivities(home, 0).map { it.activityInfo.packageName }
            if (pkg in launchers) return true
            val dial = pm.queryIntentActivities(Intent(Intent.ACTION_DIAL), 0).map { it.activityInfo.packageName }
            if (pkg in dial) return true
        } catch (_: Exception) { }
        return false
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) return
        if (!GateState.locked) return
        val pkg = event.packageName?.toString() ?: return
        if (allowed(pkg)) return
        // Çocuk başka uygulama açmaya çalışırsa anasayfaya dön ve kilit ekranını göster.
        performGlobalAction(GLOBAL_ACTION_HOME)
        val intent = Intent(this, com.mobilkontrol.ui.LockActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onInterrupt() {}

    companion object {}
}

object GateState {
    @Volatile var locked: Boolean = false
}
