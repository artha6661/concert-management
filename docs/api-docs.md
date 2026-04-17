# Concert Ticket Reservation API

Dokumentasi ini menjelaskan **semua endpoint API** yang tersedia di aplikasi.

## Informasi umum

- **Base path**: `/api`
- **Swagger UI**: `/swagger-ui`
- **OpenAPI JSON**: `/api-docs`
- **Content-Type**: `application/json`
- **Format waktu**: ISO-8601 `Instant`, contoh: `"2026-04-20T19:00:00Z"`

## Architecture

Project ini menggunakan **standard MVC / layered architecture**:
- **Controller layer**: menerima HTTP request, validasi input (`@Valid`), dan mengembalikan response.
- **Service layer (Business logic)**: berisi aturan bisnis dan orkestrasi transaksi.
- **Repository / Data access (JPA)**: query ke database.
- **Model / Entity**: representasi tabel dan rule dasar data.
- **DTO (Request/Response)**: format payload API, memisahkan entity dari contract API.
- **Enums**: berisi kelas untuk sekumpulan konstanta yang tetap
- **handler**: berisi kelas yang bertanggung jawab untuk menangani error atau exception secara global dalam aplikasi
- **config** : berisi file konfigurasi 


## Race-condition & double-booking strategy

Bagian ini menjelaskan strategi yang dipakai project ini untuk mencegah **oversell** (tiket terjual melebihi kapasitas) saat ada request paralel.

### Target masalah

- **Race condition**: banyak request booking untuk konser yang sama datang bersamaan dan masing-masing membaca `remainingTickets` yang sama.
- **Oversell**: jika update stok tidak atomik, total `CONFIRMED` bisa melebihi kapasitas.

### Strategi yang dipakai (sesuai implementasi)

1) **Transaksi di service**

- Proses booking berjalan di dalam transaksi (`@Transactional` pada `BookingService.book(...)`).

2) **Atomic conditional update (single statement)**

- Pengurangan `remainingTickets` dilakukan dengan query update yang atomik:
  - `ConcertRepository.tryDecrementRemaining(id, qty)`
  - SQL/JPQL-nya setara konsep:
    - `UPDATE concerts SET remaining_tickets = remaining_tickets - :qty`
    - `WHERE id = :id AND remaining_tickets >= :qty`
- Karena ada kondisi `remaining_tickets >= :qty`, database memastikan hanya **satu** update yang sukses untuk kapasitas yang tersisa.

3) **Keputusan CONFIRMED vs REJECTED dari hasil update**

- Jika hasil update `updated == 1` → kapasitas berhasil “dikunci” → booking disimpan sebagai `CONFIRMED`.
- Jika `updated != 1` (stok tidak cukup / kalah race) → booking disimpan sebagai `REJECTED`.
- Bila ada error akses data (`DataAccessException`) → booking disimpan sebagai `REJECTED` (fail-safe).

### Tentang “double-booking”

Istilah “double-booking” bisa berarti 2 hal:

- **A) Double-booking kapasitas (oversell)**: dicegah oleh strategi atomic conditional update di atas.
- **B) User yang sama booking berkali-kali**: saat ini **tidak ada mekanisme khusus** untuk mencegahnya (tidak ada idempotency key atau unique constraint berbasis `(concert_id, user_id)`), jadi user yang sama bisa membuat beberapa booking selama kapasitas masih ada.

Jika ingin mencegah (B), opsi umum:

- Tambahkan **idempotency key** pada endpoint booking (mis. header `Idempotency-Key`) + penyimpanan request-result.
- Atau tambah **unique constraint** pada tabel booking sesuai kebutuhan bisnis (mis. 1 booking per user per konser), dan handle conflict.
..

## Skema respons (ringkas)

### `ConcertResponse`

Field yang dikembalikan saat membaca data konser (mis. search / create):

```json
{
  "id": 1,
  "name": "My Concert",
  "venue": "Main Hall",
  "startsAt": "2026-04-20T19:00:00Z",
  "bookingOpensAt": "2026-04-15T10:00:00Z",
  "bookingClosesAt": "2026-04-15T10:20:00Z",
  "totalTickets": 10000,
  "remainingTickets": 9998
}
```

### `BookingResponse`

Field yang dikembalikan saat membuat booking:

```json
{
  "id": 123,
  "concertId": 1,
  "userId": "u-123",
  "quantity": 2,
  "status": "CONFIRMED",
  "createdAt": "2026-04-17T08:00:00Z"
}
```

Nilai `status`:

- `CONFIRMED`: booking berhasil mengunci kapasitas tiket
- `REJECTED`: booking ditolak (di luar window booking atau kapasitas tidak cukup pada saat commit)

## Error response yang umum

### 400 Bad Request (validasi input)

Terjadi bila body request tidak valid (mis. field kosong atau `quantity < 1`).

Contoh respons:

```json
{
  "timestamp": "2026-04-17T08:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "validationErrors": [
    { "field": "userId", "message": "must not be blank" },
    { "field": "quantity", "message": "must be greater than or equal to 1" }
  ]
}
```

### 404 Not Found

Terjadi bila resource tidak ditemukan (contoh: `concertId` tidak ada).

Contoh respons:

```json
{
  "timestamp": "2026-04-17T08:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "..." 
}
```

## API: Concerts

### 1) Search konser

`GET /api/concerts`

Query parameter (opsional):

- `q`: pencarian bebas yang mencocokkan `name` atau `venue`
- `startsFrom`: filter `startsAt >= startsFrom` (ISO-8601 datetime)
- `startsTo`: filter `startsAt <= startsTo` (ISO-8601 datetime)

Contoh:

`GET /api/concerts?q=hall&startsFrom=2026-04-20T00:00:00Z&startsTo=2026-04-30T23:59:59Z`
`GET /api/concerts`
`GET /api/concerts?q=Test`
`GET /api/concerts?startsFrom=2026-04-01T00:00:00Z&startsTo=2026-04-30T23:59:59Z`


Respons:

- `200 OK`: array `ConcertResponse`

Contoh:
```json
[
    {
        "id": 3,
        "name": "Test",
        "venue": "Hall",
        "startsAt": "2026-05-01T19:00:00Z",
        "bookingOpensAt": "2026-04-15T14:00:00Z",
        "bookingClosesAt": "2026-04-15T16:00:00Z",
        "totalTickets": 10,
        "remainingTickets": 10
    },
    {
        "id": 4,
        "name": "Test Concert",
        "venue": "Main Hall",
        "startsAt": "2026-05-01T19:00:00Z",
        "bookingOpensAt": "2026-04-15T14:00:00Z",
        "bookingClosesAt": "2026-04-15T23:00:00Z",
        "totalTickets": 10,
        "remainingTickets": 8
    }
]
```

## API: Admin (helper)

Catatan: endpoint ini disediakan sebagai “admin helper” untuk membuat konser (tidak ada autentikasi/otorisasi di project ini).

### 2) Buat konser baru

`POST /api/admin/concerts`

Body:

```json
{
  "name": "My Concert",
  "venue": "Main Hall",
  "startsAt": "2026-04-20T19:00:00Z",
  "bookingOpensAt": "2026-04-15T10:00:00Z",
  "bookingClosesAt": "2026-04-15T10:20:00Z",
  "totalTickets": 10000
}
```

Aturan validasi:

- `name`: wajib, tidak boleh kosong
- `venue`: wajib, tidak boleh kosong
- `startsAt`: wajib
- `bookingOpensAt`: wajib
- `bookingClosesAt`: wajib
- `totalTickets`: minimal 0

Respons:

- `201 Created`: `ConcertResponse`
- `400 Bad Request`: validasi gagal

## API: Bookings

### 3) Booking tiket konser (concurrency-safe)

`POST /api/concerts/{concertId}/bookings`

Path parameter:

- `concertId`: id konser

Body:

```json
{
  "userId": "u-123",
  "quantity": 2
}
```

Aturan validasi:

- `userId`: wajib, tidak boleh kosong
- `quantity`: minimal 1

Respons:

- `201 Created`: `BookingResponse`
  - `status=CONFIRMED` hanya jika:
    - request berada di dalam window booking: \([bookingOpensAt, bookingClosesAt)\)
    - `remainingTickets` masih cukup **pada saat commit** (aman dari race condition / oversell)
  - jika tidak memenuhi syarat di atas, `status=REJECTED`
- `400 Bad Request`: validasi gagal
- `404 Not Found`: `concertId` tidak ditemukan

