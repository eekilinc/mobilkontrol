# MobilKontrol

Çocukların Android cihaz kullanım süresini ebeveyn tarafından sınırlayan ebeveyn kontrol / ekran süresi uygulaması.

## Mimari

- **UI:** Jetpack Compose (Material 3), screens `ui/` altında
- **ViewModel:** `ui/MainViewModel.kt`
- **Repository:** `data/repository/ParentalRepository.kt`
- **Local veri:** Room (`data/local/AppDatabase.kt`) + DataStore (`data/local/SettingsStore.kt`)
- **Servis:** `service/ScreenTrackingService.kt` (foreground, ekran açıkken sayaç)
- **Takip:** `tracking/UsageStatsTracker.kt` (öncelikli) / fallback ekran-açık süresi
- **İleri mod:** `tracking/AdvancedModeManager.kt` (Device Owner / LockTask)
- **Çalışma:** `receiver/` (boot, screen, time change, admin), `work/UsageCheckWorker.kt`
- **Uzaktan kontrol:** `remote/RemoteControlServer.kt` (NanoHTTPD, PIN korumalı yerel HTTP: `/status`, `/addtime`, `/lock`, `/unlock`)
- **Uygulama engelleme:** `service/AppGateService.kt` (Accessibility Service, süre dolunca diğer uygulamaları engeller)
- **Ayarlar:** uyarı açık/kapalı, uzaktan kontrol, erişilebilirlik izni yönlendirmesi

## Derleme

```
export JAVA_HOME=/path/to/jdk-17
./gradlew :app:assembleDebug
```

APK: `app/build/outputs/apk/debug/app-debug.apk` / `app/build/outputs/apk/release/app-release.apk`

İmza: `app/release.keystore` (kullanıcı: mobilkontrol, şifre: mobilkontrol123) — geliştirme amaçlıdır.

## Test

Bkz. `docs/TEST_SCENARIOS.md`.
