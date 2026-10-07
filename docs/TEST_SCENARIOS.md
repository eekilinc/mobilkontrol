# Test Senaryoları (plan.md Bölüm 25)

Her senaryo el ile test edilmelidir:

1. **10 dakika kullanım, ekran kapatma** — Kullandığı süre doğru sayılmalı (`DailyUsageEntity.usedMinutes`).
2. **Birkaç saat sonra tekrar açma** — Sayım kaldığı yerden devam etmeli; ekran kapalı süre sayılmamalı.
3. **Arka plan** — Uygulama arka plandayken de tick'ler çalışır; kullanım artışı olmuyorsa sayaç artmamalı.
4. **Yeniden başlatma** — `BootReceiver` servisi başlatır; Room'daki günlük kullanım korunmuş olmalı.
5. **00:00 sıfırlama** — Gece yarısı geçişinde `usedMinutes=0, bonusMinutes=0, manualLock=false`.
6. **00:00'da kapalı, sabah açılış** — `ensureToday()` tarih değişimini algılayıp sıfırlama yapar.
7. **Ebeveyn limit değiştirme** — `SettingsStore.dailyLimit` güncellenir, kalan süre yeniden hesaplanır.
8. **Ek süre** — `addBonusMinutes` ile `usedMinutes` değişmez, kalan süre artar; ertesi gün `bonusMinutes=0`.
9. **Manuel kilit** — `manualLock=true` olunca servis `LockActivity` gösterir; PIN ile açılır.
10. **Uygulama kapatma denemesi** — Foreground service yeniden başlatılmalı; data silinene kadar ayarlar korunmalı.
11. **Tarih/saat değişimi** — `tick()` içinde `wallDelta` vs `elapsedDelta` farkı 10 sn'den büyükse o tick'te sayaç artırılmaz, anchor yenilenir.
12. **Süre sıfıra ulaşma** — `LockActivity` tam ekran kilit; ebeveyn PIN'i ile `manualLock=false` ve ekran açık kalır (ek süre verilene kadar servis tekrar kilitleyebilir).

Otomasyon için öneri: `ParentalRepository.ensureToday()` ve `remainingMinutes()` mantıklarının JVM unit testleri (`app/src/test`).
