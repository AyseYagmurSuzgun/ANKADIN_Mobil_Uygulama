# 📱 ANKADIN – Kişisel Güvenlik ve Acil Durum Uygulaması

ANKADIN, kullanıcıların kendilerini tehlikede hissettikleri anlarda tek bir tuşla önceden belirledikleri acil durum kişilerine konum ve yardım mesajı gönderebildikleri, aynı zamanda kritik sağlık bilgilerini güvenli şekilde saklayabildikleri bir Android mobil uygulamasıdır.

Uygulama; konum servisleri, video kaydı, harita entegrasyonu, yerel ve bulut veritabanı desteği ile kapsamlı bir kişisel güvenlik çözümü sunmayı amaçlamaktadır.

---

# 🚀 Uygulama Akışı

## 1️⃣ Splash Screen
- Uygulama açıldığında logo ve geri sayım (sayaç) içeren bir splash ekranı gösterilir.
- Sayaç tamamlandıktan sonra kullanıcı giriş ekranına yönlendirilir.

---

## 2️⃣ Kimlik Doğrulama

### 🔐 Giriş Yap
- Firebase Authentication ile kullanıcı doğrulaması yapılır.

### 📝 Kayıt Ol
Kullanıcıdan:
- İsim
- Geçerli formatta Gmail adresi
- Güçlü şifre (minimum güvenlik kriterleri)

istenir.

Geçersiz e-posta veya zayıf şifre durumunda kayıt işlemi engellenir.

Kullanılan servisler:
- Firebase Authentication
- Firebase Firestore

---

## 3️⃣ Ana Ekran – Bottom Navigation (4 Fragment)

Uygulama giriş sonrası 4 ana bölümden oluşur:

---

## 🆘 1. Ana Sayfa (SOS Ekranı)

- Büyük bir **Acil Yardım Butonu**
- Manuel konum bilgisi gösterimi

### SOS Butonuna Basıldığında:

- (Ayar tercihe bağlı olarak) video kaydı başlatılır
- Kayıtlı acil durum kişilerine SMS ile yardım mesajı gönderilir
- Harita ekranına yönlendirme yapılır
- En yakın hastane veya karakola yol tarifi alınabilir

---

## 👤 2. Profil Sayfası

Kullanıcı kişisel ve sağlık bilgilerini girer.

### Kişisel Bilgiler
- İsim
- TC
- Telefon
- Yaş
- Cinsiyet
- Doğum Tarihi (Takvim seçimi)

### Sağlık Bilgileri
- Kan grubu (Liste seçimi)
- Alerjiler
- Kullanılan ilaçlar
- Kronik hastalıklar

Veriler:
- Room Database ile yerel olarak saklanır
- Firebase Firestore ile bulutta tutulur

---

## 👥 3. Acil Durum Kişileri

- En fazla 3 kişi eklenebilir
- Galeriden fotoğraf seçilebilir (izin kontrolü yapılır)
- İsim ve telefon bilgisi girilir
- RecyclerView ile listelenir

Bir kişiye tıklandığında:
- Güncelleme
- Silme
- Gönderilecek acil durum mesajını görüntüleme

---

## 🗺 4. Harita Sayfası

- Kullanıcının manuel konumu marker ile gösterilir
- Butonlarla:
  - En yakın hastane
  - En yakın karakol

harita üzerinde ayrı ayrı gösterilir.

Marker’a tıklandığında:
- Google Maps uygulaması açılır
- Mevcut konumdan yol tarifi alınır

---

# ⚙️ Options Menu (3 Nokta Menü)

### 📄 Hakkında
Uygulama hakkında bilgilendirme sayfası.

### ⚙️ Ayarlar
- Acil durum mesajını düzenleme
- SOS butonunda video kaydı aç/kapat
- Bildirimleri aç/kapat
- Koyu mod desteği
- Destek (mail uygulamasına yönlendirme)

### 🚪 Çıkış
- Firebase oturumu kapatılır
- Kullanıcı giriş ekranına yönlendirilir

---

# 🛠 Kullanılan Teknolojiler ve Konular

- Kotlin
- MVVM Mimarisi
- Fragment tabanlı yapı
- ViewBinding
- Firebase Authentication
- Firebase Firestore
- Room Database
- Google Maps SDK
- Google Play Services Location
- RecyclerView
- ListView
- SharedPreferences
- Intent Kullanımı
- Runtime Permission Yönetimi
- Coroutines
- Splash Screen & Sayaç
- Options Menu

---

# 🧠 Mimari Yaklaşım

- MVVM mimari yapısı
- Yerel + Bulut veri yönetimi
- Kullanıcı tercihleri için SharedPreferences
- Modüler ve okunabilir kod yapısı

---

# 📸 Uygulama Görselleri

## 🟣 Splash Screen
<p align="center">
  <img src="https://github.com/user-attachments/assets/59ae9af3-fdd7-4592-a341-f65169bad4ac" width="250"/>
</p>

---

## 🔐 Login & Register
<p align="center">
  <img src="https://github.com/user-attachments/assets/b6959b48-6725-4365-8e71-02892a392581" width="250"/>
  <img src="https://github.com/user-attachments/assets/869c943e-0fe1-4fa4-8570-3c3e6a526ead" width="250"/>
</p>

---

## 🆘 Ana Sayfa (SOS)
<p align="center">
  <img src="https://github.com/user-attachments/assets/6b584b48-9070-4d00-bf6e-e7dfdd6fead2" width="250"/>
  <img src="https://github.com/user-attachments/assets/a96ddc50-024b-417d-9981-ff0660f17c20" width="250"/>
</p>

---

## 👤 Profil
<p align="center">
  <img src="https://github.com/user-attachments/assets/2deaeaf3-dd5a-4d88-9412-7370aebd0c65" width="250"/>
  <img src="https://github.com/user-attachments/assets/504ab284-8c5a-46c9-8215-7402633413ef" width="250"/>
</p>

---

## 👥 Acil Durum Kişileri
<p align="center">
  <img src="https://github.com/user-attachments/assets/85c97528-a83b-4201-8120-fbce98cdc5f7" width="250"/>
</p>

---

## 🗺 Harita
<p align="center">
  <img src="https://github.com/user-attachments/assets/f5f6b425-94f1-4e13-9a29-9d769892a4b6" width="250"/>
  <img src="https://github.com/user-attachments/assets/88380744-7f55-4105-afc7-e3aa17bc641e" width="250"/>
  <img src="https://github.com/user-attachments/assets/f10a4557-0e6d-4bb4-85ae-651cb7e400ff" width="250"/>
</p>

---

## ℹ️ Hakkında
<p align="center">
  <img src="https://github.com/user-attachments/assets/0fcb574d-d1ec-43db-b9da-721ad8240946" width="250"/>
</p>

---

## ⚙️ Ayarlar
<p align="center">
  <img src="https://github.com/user-attachments/assets/20641caa-7818-4abe-a29a-001fb55bd1de" width="250"/>
</p>

---

# 🔮 Geliştirilebilir Alanlar

- Canlı konum takibi
- Otomatik konum algılama
- Gerçek zamanlı konum paylaşımı
- Push Notification sistemi
- Acil durum geçmişi kaydı
- Wear OS entegrasyonu

---

# 📌 Bilinen Sınırlamalar

- Konum manuel girilmektedir
- Canlı konum takibi bulunmamaktadır
- Video kaydı arka planda sürekli çalışmamaktadır
 
---

## 🚀 Kurulum ve Başlangıç

Projeyi yerel makinenizde çalıştırmak için aşağıdaki adımları izleyin.

### Gereksinimler

- Android Studio 
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
          android:value="YOUR_GOOGLE_API_KEY" />
      ```

5.  **Uygulamayı Çalıştırın:**
    Tüm adımları tamamladıktan sonra uygulamayı bir emülatörde veya fiziksel bir cihazda çalıştırabilirsiniz.

---

# 👩‍💻 Geliştirici

Ayşe Yağmur Süzgün  

## 📜 Lisans

Bu proje [MIT Lisansı](https://opensource.org/licenses/MIT) altında lisanslanmıştır.


