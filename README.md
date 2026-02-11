# ANKADIN - Kişisel Güvenlik ve Acil Durum Uygulaması

ANKADIN, kullanıcıların kendilerini tehlikede hissettikleri anlarda, önceden belirledikleri acil durum kişilerine tek bir tuşla konumlarını ve yardım mesajlarını SMS ile göndermelerini sağlayan bir mobil uygulamadır.

Uygulama, sadece bir yardım butonu olmanın ötesinde, kullanıcıların kritik sağlık bilgilerini (kan grubu, alerjiler, hastalıklar vb.) saklamalarına olanak tanıyarak olası bir müdahale durumunda ilk yardım ekiplerine hayati bilgiler sunar.

## ✨ Temel Özellikler

- **🚨 Tek Tuşla SOS:** Ana ekrandaki büyük SOS butonu ile acil durum mesajını ve anlık konumu saniyeler içinde gönderin.
- **👥 Acil Durum Kişileri Yönetimi:** Güvendiğiniz kişileri listenize ekleyin, düzenleyin veya silin. SOS mesajı bu kişilere gönderilir.
- **❤️ Detaylı Sağlık Profili:** Kan grubu, alerjiler, kullanılan ilaçlar, kronik hastalıklar gibi hayati önem taşıyan bilgileri profilinize kaydedin.
- **🔐 Firebase ile Güvenli Giriş:** Kullanıcı hesapları Firebase Authentication ile güvenli bir şekilde yönetilir.
- **☁️ Bulut Veri Saklama:** Kullanıcı verileri (profil, acil durum kişileri vb.) Firebase Firestore üzerinde saklanır.
- **📱 Çevrimdışı Destek:** Kritik sağlık profili bilgileri Room veritabanı sayesinde çevrimdışı durumlarda bile erişilebilirdir.
- **🗺️ Konum Servisleri:** Google Maps entegrasyonu ile anlık konumunuzu harita üzerinde görüntüleyin.
- **🌙 Koyu Mod Desteği:** Cihaz ayarlarına duyarlı, göz yormayan modern bir koyu tema arayüzü.

## 🛠️ Teknoloji Mimarisi ve Kütüphaneler

- **Dil:** %100 [Kotlin](https://kotlinlang.org/)
- **Mimari:** MVVM (Model-View-ViewModel) - Fragment tabanlı UI yapısı
- **Asenkron Programlama:** Coroutines
- **UI:**
    - Android Views & XML
    - [Material Components](https://material.io/develop/android): Modern ve tutarlı bir tasarım dili için.
- **Veritabanı:**
    - **Yerel:** [Room Persistence Library](https://developer.android.com/training/data-storage/room) - Kullanıcı sağlık profili gibi kritik verileri çevrimdışı erişim için cihazda saklar.
    - **Uzak:** [Firebase Firestore](https://firebase.google.com/docs/firestore) - Kullanıcı, acil durum kişileri ve diğer verileri bulutta saklamak için.
- **Kimlik Doğrulama:** [Firebase Authentication](https://firebase.google.com/docs/auth)
- **Harita & Konum:**
    - [Google Maps SDK](https://developers.google.com/maps/documentation/android-sdk)
    - [Google Play Services Location](https://developers.google.com/android/reference/com/google/android/gms/location/package-summary)

## 🚀 Kurulum ve Başlangıç

Projeyi yerel makinenizde çalıştırmak için aşağıdaki adımları izleyin.

### Gereksinimler

- Android Studio (Iguana veya üstü tavsiye edilir)
- JDK 17

### Adımlar

1.  **Projeyi Klonlayın:**
    ```sh
    git clone https://github.com/kullanici-adiniz/ANKADIN4.git
    ```

2.  **Android Studio'da Açın:**
    Projeyi Android Studio'da açın ve Gradle senkronizasyonunun tamamlanmasını bekleyin.

3.  **Firebase Kurulumu (ÖNEMLİ):
    Bu proje çalışmak için Firebase servislerine ihtiyaç duyar.
    - [Firebase Console](https://console.firebase.google.com/) üzerinden yeni bir proje oluşturun.
    - Proje ayarlarına gidin ve `com.example.ankadin` paket adına sahip yeni bir Android uygulaması ekleyin.
    - Oluşturulan `google-services.json` dosyasını indirin ve projenizin `app/` dizini altına yapıştırın.
    - Firebase konsolundan **Authentication** ve **Firestore Database** servislerini aktif edin.

4.  **Google Maps API Anahtarı (ÖNEMLİ):
    Haritanın çalışması için bir API anahtarına ihtiyacınız var.
    - [Google Cloud Console](https://console.cloud.google.com/) üzerinden bir proje oluşturun ve **Maps SDK for Android**'i etkinleştirin.
    - Bir API anahtarı oluşturun.
    - Oluşturduğunuz anahtarı `app/src/main/AndroidManifest.xml` dosyasındaki ilgili alana yapıştırın:
      ```xml
      <meta-data
          android:name="com.google.android.geo.API_KEY"
          android:value="BURAYA_KENDİ_API_ANAHTARINIZI_YAPIŞTIRIN" />
      ```

5.  **Uygulamayı Çalıştırın:**
    Tüm adımları tamamladıktan sonra uygulamayı bir emülatörde veya fiziksel bir cihazda çalıştırabilirsiniz.

## 📝 Gelecek Planları ve İyileştirmeler

- [ ] Yakındaki hastane, eczane ve karakolları haritada gösterme.
- [ ] Farklı acil durum senaryoları için (yangın, deprem vb.) özelleştirilmiş mesaj şablonları.
- [ ] Panik anında yanlış basımları önlemek için SOS butonuna basılı tutma veya kaydırma özelliği.
- [ ] Giyilebilir cihazlar (Wear OS) için bir tamamlayıcı uygulama.
- [ ] Çevrimdışı harita desteği.

## 📜 Lisans

Bu proje [MIT Lisansı](https://opensource.org/licenses/MIT) altında lisanslanmıştır.
