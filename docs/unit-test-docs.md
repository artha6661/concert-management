# Dokumentasi Testing (Versi Sederhana)

Project ini dibuat dengan **Spring Boot (Maven)**. Semua kode untuk testing ada di folder:

```
src\test\java\
```

## Cara Menjalankan Test

* **Menjalankan semua test sekaligus:**

```bash
mvn test
```

* **Menjalankan satu file test saja (misalnya):**

```bash
mvn -Dtest=BookingConcurrencyTest test
```

* **Menjalankan satu fungsi test tertentu:**

```bash
mvn -Dtest=BookingConcurrencyTest#onlyCapacityWorthOfBookingsCanConfirm test
```

---

## Struktur dan Lokasi Test

### 1. Test untuk API (Endpoint)

Lokasi:

```
src\test\java\com\edts\concert_ticket_reservation\api\
```

Isi test-nya:

* **ConcertControllerTest** → untuk mengecek fitur pencarian konser
* **AdminConcertControllerTest** → untuk mengecek fitur admin membuat konser
* **BookingControllerTest** → untuk memastikan booking berhasil (status `CONFIRMED`)
* **BookingControllerRejectedTest** → untuk memastikan booking gagal (status `REJECTED`), misalnya:

  * di luar waktu booking
  * tiket sudah habis

---

### 2. Test untuk Service (Logika Utama)

Lokasi:

```
src\test\java\com\edts\concert_ticket_reservation\service\
```

* **BookingConcurrencyTest**
  Digunakan untuk mengetes kondisi saat banyak orang booking **secara bersamaan** (agar tidak terjadi overselling / tiket terjual lebih dari kapasitas).

---

### 3. Konfigurasi Test

Lokasi:

```
src\test\resources\
```

* **application-test.yml**
  Berisi konfigurasi khusus untuk test, seperti:

  * database sementara (H2, tidak pakai database asli)
  * migrasi database otomatis (Flyway)

---

## Jenis Test di Project Ini

Sebagian besar test di sini menggunakan `@SpringBootTest`.

Artinya:

* Test dijalankan seperti aplikasi sungguhan (full system)
* Bukan unit test murni (yang biasanya hanya test 1 class dengan mock)

Jadi lebih tepat disebut:
👉 **Integration Test (test level aplikasi)**

---

## Test Race Condition (Booking Barengan)

Kalau banyak user booking tiket di waktu yang sama, bisa terjadi masalah seperti:

* tiket terjual lebih dari kapasitas (oversell)

Test untuk menangani ini ada di:

```
BookingConcurrencyTest.java
```

Test ini akan:

* Menjalankan banyak request booking secara bersamaan
* Memastikan:

  * jumlah booking yang berhasil (`CONFIRMED`) **tidak melebihi kapasitas**
  * sisanya otomatis ditolak (`REJECTED`)
  * tiket habis (`remainingTickets = 0`)

---

## Apakah Ini Sudah Load Test?

Belum.

Test ini hanya memastikan logika benar saat kondisi bersamaan, tapi **tidak mengukur performa** seperti:

* seberapa cepat sistem merespon
* berapa banyak request yang bisa ditangani
* simulasi traffic dunia nyata

---

## Kalau Mau Load Test Sungguhan

Load test sebaiknya dipisahkan dari test biasa.

### Dengan Tools Tambahan

Buat folder baru di root project:

```
load-test/
```

Isi dengan tools seperti:

* k6
* JMeter
* wrk

## Kesimpulan

* Project ini sudah punya:

  * test API
  * test service
  * test concurrency (anti oversell)
* Tapi belum punya:

  * load test terpisah

Untuk saat ini, test race condition ada di:
👉 **BookingConcurrencyTest**
