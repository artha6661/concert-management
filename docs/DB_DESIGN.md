# Desain Database

## Tabel-Tabel

### `concerts` (Konser)

- **Fungsi**: Menyimpan data inventori tiket dan jendela pemesanan konser
- **Field Utama**
  - `starts_at`: Waktu konser dimulai
  - `booking_opens_at`, `booking_closes_at`: Jendela waktu pemesanan dapat dilakukan (penutupan bersifat eksklusif, artinya pemesanan ditutup tepat di waktu tersebut)
  - `total_tickets`, `remaining_tickets`: Counter jumlah total tiket dan tiket yang masih tersedia
  - `version`: Kolom untuk optimistic locking (tersedia untuk penggunaan di masa depan)

### `bookings` (Pemesanan)

- **Fungsi**: Menyimpan catatan permanen dari setiap percobaan pemesanan (tidak dapat diubah)
- **Field Utama**
  - `concert_id`: Foreign key yang merujuk ke tabel `concerts`
  - `user_id`: Identitas pengguna yang melakukan pemesanan (disederhanakan)
  - `quantity`: Jumlah tiket yang diminta
  - `status`: Status pemesanan, berupa `CONFIRMED` (berhasil) atau `REJECTED` (ditolak)

## Strategi Pengelolaan Concurrency (Akses Bersamaan)

Stok tiket dikurangi menggunakan atomic conditional update (operasi yang tidak terbagi dan bersyarat):

```sql
UPDATE concerts 
SET remaining_tickets = remaining_tickets - :qty 
WHERE id = :id AND remaining_tickets >= :qty
```

Penjelasan:
- Hanya **satu transaksi** yang dapat berhasil mengurangi stok untuk konser yang sama pada waktu yang sama
- Transaksi yang berhasil akan mencatat status `CONFIRMED`
- Transaksi yang gagal (karena stok tidak cukup) akan mencatat status `REJECTED`
- Pendekatan ini menjamin **tidak ada overbooking** (penjualan tiket melebihi stok yang tersedia)

