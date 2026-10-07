# Firebase App Distribution (test kullanıcılarına dağıtım)

Play Store'a girmeden APK'yı test kullanıcılarına gönderir. Play Protect "güvenilmeyen geliştirici" uyarısı gelmez (ilk kurulumda yine bir onay ekranı çıkabilir).

## 1) Firebase Console kurulumu (siz yapacaksınız)

1. https://console.firebase.google.com → **Proje ekle** → ad: `mobilkontrol`
2. Projeyi açın → **Android uygulaması ekle**:
   - Paket adı: `com.mobilkontrol`
   - (debug/release APK yüklemeniz istenirse atlayın)
3. Sol menü → **App Distribution** → **Başla** → tester grubu oluşturun (`testers`)
   - **Testerlar** sekmesinden kendinizin e-postasını ekleyin
4. **Proje ayarları → Hizmet hesapları**:
   - **Hizmet hesabı oluştur** → Firebase App Distribution yönetici yetkisi verin
   - **Anahtar oluştur (JSON)** → indirilen `xxx.json` dosyasının içeriğini kopyalayın

## 2) GitHub Secrets

`Settings → Secrets and variables → Actions → New repository secret`:

| Secret | Değer |
|---|---|
| `FIREBASE_SERVICE_ACCOUNT` | `xxx.json` dosyasının **tüm içeriği** (tek satır, `{...}`) |
| `FIREBASE_PROJECT_ID` | `mobilkontrol` |

## 3) Kullanım

- `main` push veya `v*` tag → CI imzalı release APK'yı Firebase'e yükler
- Firebase Console → App Distribution → **Releases** sekmesinden "Yeni sürümü yükle" linki
- Testerlar bu linke girip APK'yı indirip kurar (ilk seferde **Firebase App** uygulamasını kurmalar)

## Notlar

- AAB (Android App Bundle) gerekmez; APK yeterlidir.
- Erişilebilirlik servisi (AppGateService) yine Ayarlar'dan manuel etkinleştirilir.
- Debug APK artifact olarak Actions'a yüklenmeye devam eder.