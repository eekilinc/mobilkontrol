Android telefon ve tabletlerde çalışacak, çocukların cihaz kullanım süresini ebeveyn tarafından sınırlandırmaya yarayan modern ve güvenli bir **Ebeveyn Kontrol / Ekran Süresi Uygulaması** geliştir.

## 1. Uygulamanın temel amacı

Uygulama ilk kez telefon veya tablete kurulduğunda ebeveyn tarafından yapılandırılacak.

Ebeveyn;

* Günlük maksimum cihaz kullanım süresini belirleyebilmeli.
* Örneğin günlük **1 saat, 2 saat, 3 saat veya özel süre** tanımlayabilmeli.
* Çocuk cihazı kullandıkça kalan süre otomatik olarak azalmalı.
* Günlük kullanım hakkı bittiğinde cihaz kullanımı sınırlandırılmalı.
* Kullanım süresi **her gece 00:00'da otomatik olarak sıfırlanmalı**.
* Ertesi gün çocuk yeniden belirlenen günlük süre kadar cihazı kullanabilmeli.

Örnek:

Ebeveyn günlük kullanım süresini:

`1 saat`

olarak belirledi.

Çocuk;

09:00–09:20 → 20 dakika kullandı.

Kalan:

`40 dakika`

Daha sonra;

15:00–15:30 → 30 dakika kullandı.

Kalan:

`10 dakika`

Son 10 dakika da kullanıldığında günlük kullanım hakkı tamamlanır.

Saat 00:00 olduğunda sayaç otomatik sıfırlanır ve yeni gün için tekrar:

`1 saat`

kullanım hakkı tanımlanır.

---

## 2. İlk kurulum ekranı

Uygulama ilk açıldığında bir kurulum sihirbazı göster.

Adımlar:

1. Hoş Geldiniz ekranı
2. Ebeveyn PIN kodu oluşturma
3. Günlük kullanım süresini belirleme
4. Gerekli Android izinlerini verme
5. Korunacak cihazı etkinleştirme
6. Kurulumu tamamlama

Ebeveyn en az **4 veya 6 haneli PIN** belirleyebilsin.

Örnek:

`Ebeveyn PIN: 2580`

Bu PIN olmadan çocuk ayarlara erişememeli.

---

## 3. Ebeveyn paneli

Ana ebeveyn ekranında büyük ve anlaşılır kartlar kullan.

Gösterilecek bilgiler:

**Bugünkü Kullanım**

Örneğin:

`43 dakika`

**Günlük Limit**

`1 saat`

**Kalan Süre**

`17 dakika`

Ayrıca ilerleme çubuğu göster.

Örneğin:

`43 / 60 dakika`

Ekranda şu seçenekler bulunsun:

* Günlük kullanım süresi
* Bugünkü kullanım
* Kalan süre
* Süre ekle
* Cihazı geçici olarak aç
* Cihazı kilitle
* Günlük istatistikler
* Haftalık istatistikler
* Ayarlar

---

## 4. Günlük kullanım süresi ayarlama

Hazır seçenekler oluştur:

* 30 dakika
* 45 dakika
* 1 saat
* 1 saat 30 dakika
* 2 saat
* 3 saat
* Özel süre

Özel süre seçildiğinde ebeveyn saat ve dakika girebilsin.

Örneğin:

`1 saat 20 dakika`

---

## 5. Kullanım süresi hesaplama

Sadece cihaz gerçekten kullanılırken süre azaltılmalı.

Telefon/tablet ekranı kapalıysa kullanım süresinden düşülmemeli.

Cihaz aktif olarak kullanıldığında sayaç ilerlemeli.

Uygulama arka plana alınsa bile kullanım süresi doğru şekilde takip edilmeli.

Telefon yeniden başlatıldığında sayaç kaybolmamalı.

Kullanılan süre cihazın yerel veritabanında güvenli şekilde saklanmalı.

---

## 6. Günlük sıfırlama sistemi

Her gece:

`00:00`

olduğunda günlük kullanım süresi otomatik olarak sıfırlansın.

Örneğin:

Günlük limit:

`60 dakika`

Dün kullanılan:

`60 dakika`

Saat 00:00 sonrasında:

Bugünkü kullanım:

`0 dakika`

Kalan süre:

`60 dakika`

olsun.

Telefon gece kapalıysa ve sabah açılırsa uygulama tarih değişimini kontrol ederek yeni günü otomatik başlatmalı.

Sadece zamanlayıcıya güvenme.

Her açılışta:

`son kullanım tarihi != bugünün tarihi`

kontrolü yap.

Tarih değişmişse günlük sayaç sıfırlansın.

---

## 7. Süre bitmeden uyarılar

Çocuğa kalan süre konusunda bildirim göster.

Örneğin:

### 15 dakika kaldığında

`Bugünkü kullanım sürenizin bitmesine 15 dakika kaldı.`

### 5 dakika kaldığında

`Kullanım sürenizin bitmesine 5 dakika kaldı.`

### 1 dakika kaldığında

`Kullanım sürenizin bitmesine 1 dakika kaldı.`

Süre tamamlandığında:

`Bugünkü kullanım süreniz doldu.`

mesajı göster.

---

## 8. Süre dolduğunda gösterilecek ekran

Günlük kullanım süresi sona erdiğinde çocuk cihazı normal şekilde kullanamamalı.

Tam ekran bir kilit ekranı göster.

Ekranda:

### Bugünkü kullanım süren doldu

`Yarın tekrar kullanabilirsin.`

Altında:

`Ebeveyn Girişi`

butonu olsun.

Ebeveyn PIN kodunu girerse cihaz üzerinde ebeveyn seçenekleri açılmalı.

---

## 9. Ebeveyn süre ekleme özelliği

Ebeveyn PIN girerek çocuğa ek süre verebilsin.

Hazır seçenekler:

* +5 dakika
* +10 dakika
* +15 dakika
* +30 dakika
* +1 saat
* Özel süre

Örneğin çocuk günlük 60 dakikasını kullandı.

Ebeveyn:

`+15 dakika`

verirse o gün için toplam hak geçici olarak:

`75 dakika`

olsun.

Ancak ertesi gün tekrar normal günlük limite dönsün.

---

## 10. Molaya alma özelliği

Ebeveyn isterse süre sayacını geçici olarak durdurabilsin.

Örneğin:

`Süreyi Dondur`

butonu.

Daha sonra:

`Devam Ettir`

ile yeniden başlatılabilsin.

---

## 11. Anında kilitleme

Ebeveyn panelinde:

`Cihazı Şimdi Kilitle`

butonu olsun.

Bu butona basıldığında günlük süre bitmemiş olsa bile cihaz çocuk kullanımına kapatılsın.

Ebeveyn daha sonra PIN girerek tekrar açabilsin.

---

## 12. Ebeveyn PIN güvenliği

Çocuk;

* günlük kullanım süresini değiştirememeli,
* uygulamayı kolaylıkla kapatamamalı,
* ayarları değiştirememeli,
* süre ekleyememeli,
* sayaç değerlerini değiştirememeli.

Bu işlemler için ebeveyn PIN'i gerekli olsun.

PIN doğrulaması güvenli şekilde yapılmalı.

PIN düz metin olarak saklanmamalı.

---

## 13. İstatistik ekranı

Ebeveyne günlük ve haftalık kullanım istatistikleri göster.

Örnek:

| Gün       | Kullanım |
| --------- | -------: |
| Pazartesi |    48 dk |
| Salı      |    60 dk |
| Çarşamba  |    35 dk |
| Perşembe  |    52 dk |
| Cuma      |    60 dk |
| Cumartesi |    41 dk |
| Pazar     |    37 dk |

Haftalık grafik oluştur.

Göster:

`Bu haftaki toplam kullanım`

`En fazla kullanılan gün`

`Günlük ortalama`

---

## 14. Çocuk ekranı

Çocuk için çok sade bir ekran oluştur.

Örneğin:

### Bugünkü Kalan Süren

# 37 dakika

Altında dairesel bir ilerleme göstergesi bulunsun.

Ayrıca:

`Bugün 23 dakika kullandın.`

yazsın.

---

## 15. Arayüz tasarımı

Modern Android tasarım yaklaşımı kullan.

Arayüz;

* sade,
* büyük butonlu,
* çocuk ve ebeveyn için anlaşılır,
* telefon ve tablet ekranlarına uyumlu,
* responsive

olmalı.

Tabletlerde kartlar iki sütunlu gösterilebilir.

Telefonlarda tek sütun kullanılabilir.

Material Design 3 yaklaşımı kullanılabilir.

---

## 16. Teknik yapı

Uygulamayı mümkün olduğunca modern Android mimarisiyle geliştir.

Tercih edilen teknolojiler:

* Kotlin
* Jetpack Compose
* MVVM
* Room Database
* DataStore
* WorkManager
* Coroutines
* Flow / StateFlow
* Material Design 3

Kod modüler ve okunabilir olsun.

Katmanlar mümkün olduğunca:

* UI
* ViewModel
* Repository
* Database
* Service
* Usage Tracking
* Parental Control

şeklinde ayrıştırılsın.

---

## 17. Kullanım takibi

Android'in izin verdiği sistem API'lerini kullanarak cihaz kullanımını takip et.

Gerekirse:

* UsageStatsManager
* Foreground Service
* WorkManager
* Accessibility Service
* Device Policy API

gibi Android mekanizmalarını değerlendir.

Ancak mümkün olan her yerde resmi Android API'lerini tercih et.

Gereksiz veya riskli izin kullanma.

---

## 18. Android kısıtlamalarını dikkate al

Normal bir Android uygulamasının tüm telefonu koşulsuz olarak kilitlemesi veya sistem uygulamalarını engellemesi Android güvenlik modeli nedeniyle her cihazda mümkün olmayabilir.

Bu nedenle geliştirme sırasında iki çalışma modu tasarla:

### Standart Mod

Normal Android uygulaması olarak çalışır.

* kullanım süresini takip eder,
* uyarılar gösterir,
* süre dolunca kilit ekranı gösterir,
* izin verilen Android mekanizmalarıyla uygulama kullanımını sınırlar.

### Gelişmiş Ebeveyn Kontrol Modu

Desteklenen cihazlarda;

* Device Owner,
* Device Policy Manager,
* Lock Task / Kiosk Mode

gibi Android kurumsal/ebeveyn kontrol özellikleri değerlendirilebilir.

Uygulama mimarisini ileride bu gelişmiş moda geçilebilecek şekilde tasarla.

---

## 19. Uygulamanın kapanmasına karşı koruma

Telefon yeniden başlatıldığında uygulama gerekli servisleri yeniden başlatabilsin.

Boot Completed mekanizmasını Android'in izin verdiği şekilde kullan.

Telefon kapatılıp açıldığında:

* ebeveyn ayarları,
* günlük limit,
* bugünkü kullanım,
* ek süre,
* kilit durumu

kaybolmamalı.

---

## 20. Tarih ve saat manipülasyonu

Çocuğun telefon saatini değiştirerek süreyi sıfırlamaya çalışması ihtimalini dikkate al.

Basitçe yalnızca sistem saatine güvenme.

Mümkün olan güvenli kontrolleri uygula.

Şüpheli ileri/geri saat değişikliklerini tespit etmek için sistem zamanı ve geçen süre bilgisini birlikte değerlendir.

---

## 21. İnternet gereksinimi

İlk sürüm mümkün olduğunca:

**internetsiz çalışabilsin.**

Tüm temel özellikler cihaz üzerinde çalışmalı.

Bulut hesabı zorunlu olmamalı.

İleride;

* ebeveynin kendi telefonundan uzaktan kontrol,
* çocuk cihazını uzaktan kilitleme,
* uzaktan süre ekleme,
* kullanım raporlarını görüntüleme

özelliklerinin eklenebilmesi için mimari buna uygun tasarlansın.

---

## 22. Veri modeli

Örnek veri modelleri oluştur:

### ParentalSettings

* parentPinHash
* dailyLimitMinutes
* warning15Enabled
* warning5Enabled
* warning1Enabled
* autoResetEnabled

### DailyUsage

* date
* usedMinutes
* bonusMinutes
* manualLock
* lastUpdated

### UsageHistory

* date
* totalUsageMinutes
* dailyLimitMinutes

---

## 23. Temel algoritma

Mantık yaklaşık olarak:

```text
Uygulama başlatıldı

Bugünün tarihini kontrol et

Eğer:
bugünün tarihi != kayıtlı son tarih

ise:

kullanılan süre = 0
ek süre = 0
manuel kilit = false
bugünün tarihini kaydet

Günlük toplam hak =
günlük limit + ek süre

Eğer cihaz aktif kullanılıyorsa:

kullanım süresini artır

kalan süre =
toplam hak - kullanılan süre

Eğer kalan süre <= 15 dakika:
uyarı göster

Eğer kalan süre <= 5 dakika:
uyarı göster

Eğer kalan süre <= 1 dakika:
uyarı göster

Eğer kalan süre <= 0:

çocuk kullanımını sınırla
kilit ekranını göster
```

---

## 24. Uygulama ekranları

Aşağıdaki ekranları oluştur:

1. Splash Screen
2. İlk Kurulum
3. PIN Oluşturma
4. İzin Kurulumu
5. Günlük Süre Belirleme
6. Çocuk Ana Ekranı
7. Ebeveyn Giriş Ekranı
8. Ebeveyn Kontrol Paneli
9. Günlük Süre Ayarı
10. Ek Süre Ekleme
11. Günlük İstatistik
12. Haftalık İstatistik
13. Ayarlar
14. Süre Doldu Kilit Ekranı

---

## 25. Önemli senaryoları test et

Şu senaryolar mutlaka test edilmeli:

* Çocuk cihazı 10 dakika kullanıp ekranı kapatır.
* Birkaç saat sonra tekrar açar.
* Uygulama arka planda kalır.
* Telefon yeniden başlatılır.
* Gece 00:00 olur.
* Telefon 00:00 sırasında kapalıdır ve sabah açılır.
* Ebeveyn günlük limiti değiştirir.
* Ebeveyn ek süre verir.
* Ebeveyn manuel kilit uygular.
* Çocuk uygulamayı kapatmaya çalışır.
* Çocuk tarih/saat değiştirmeye çalışır.
* Kullanım süresi tam sıfıra ulaşır.

Bu durumlarda veri kaybı veya yanlış süre hesaplaması olmamalı.

---

## 26. İlk geliştirme aşaması

Projeyi tek seferde karmaşıklaştırma.

Önce çalışan bir MVP oluştur.

### MVP 1

Öncelikle:

1. PIN oluşturma
2. Günlük süre belirleme
3. Cihaz kullanım süresini takip etme
4. Kalan süreyi gösterme
5. 00:00 günlük sıfırlama
6. Süre bitince kilit ekranı
7. Ebeveyn PIN'i ile kilidi açma
8. Ek süre verme
9. Telefon yeniden başladığında verileri koruma

özelliklerini eksiksiz çalıştır.

Daha sonra istatistikler ve gelişmiş ebeveyn kontrol özelliklerine geç.

Kod üretirken her dosyanın;

* dosya adını,
* klasör konumunu,
* görevini

belirt.

Eksik veya sözde kod verme.

Projeyi Android Studio içerisinde derlenebilir gerçek bir proje olarak oluştur.

Her aşamada gerekli:

* AndroidManifest.xml izinlerini,
* Gradle bağımlılıklarını,
* Kotlin sınıflarını,
* Compose ekranlarını,
* ViewModel yapılarını,
* servisleri,
* Room tablolarını

eksiksiz hazırla.


