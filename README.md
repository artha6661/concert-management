# Concert Ticket Reservation (EDTS Technical Assessment)

Backend APIs for concert search and ticket booking (time-window + limited inventory + concurrency-safe).

## Tech

- Java 17
- Spring Boot 3.4.3 (Maven)
- Hibernate JPA
- Flyway migrations
- PostgreSQL 15 / H2 (tests)
- OpenAPI/Swagger UI
- Maven 3.9.9

## Quickstart (local)

Start PostgreSQL:

```bash
docker compose up -d
```

Run the API:

```bash
mvn spring-boot:run
```

Open Swagger UI at `/swagger-ui` (default: `http://localhost:8080/swagger-ui/index.html`).

## Docs

- API documentation: `docs/api-docs.md`
- Database design: `docs/DB_DESIGN.md`

## Notes

- Seed data ditambahkan via Flyway (`V2__seed.sql`) untuk testing manual dengan cepat.
- Pengurangan stok tiket saat booking dilakukan secara aman dengan mekanisme update khusus, agar tiket tidak terjual melebihi jumlah yang tersedia.

## Known Limitations & Security Concerns

Proyek ini dibuat sebagai technical assessment dengan timeline yang terbatas. Karena kesibukan pekerjaan yang tinggi, beberapa aspek keamanan dan best practices belum sempat diimplementasikan dengan maksimal:

### Security Issues
- **Autentikasi & Autorisasi**: Saat ini tidak ada implementasi authentication (JWT, OAuth2, dll). Semua endpoint dapat diakses tanpa verifikasi identitas pengguna.
- **Input Validation**: Validasi input masih minimal, risiko untuk SQL injection dan malicious payloads masih terbuka.
- **API Security Headers**: Belum menambahkan security headers seperti HSTS, X-Content-Type-Options, X-Frame-Options, dll.
- **Rate Limiting**: Tidak ada mekanisme rate limiting untuk mencegah brute force attacks atau DDoS.
- **CORS Configuration**: CORS configuration masih terlalu permissive, belum di-restrict dengan proper origins.
- **Data Encryption**: Data sensitif (booking details) belum di-encrypt at-rest maupun in-transit untuk production.
- **Audit Logging**: Tidak ada comprehensive audit trail untuk tracking user actions dan security events.
- **API Key Management**: Tidak ada API key validation atau secure credential management.

### Recommendations untuk Production
1. Implementasikan authentication & authorization framework (Spring Security + JWT)
2. Tambahkan comprehensive input validation & sanitization
3. Setup security headers melalui Spring Security configuration
4. Implement rate limiting dengan tools seperti Bucket4J atau Spring Cloud
5. Configure CORS dengan specific allowed origins
6. Enable HTTPS & implement database encryption
7. Add comprehensive logging & monitoring
8. Perform security testing & penetration testing
9. Implement API versioning & deprecation strategy

Diharapkan issues tersebut dapat ditangani pada fase implementation setelah timeline assessment ini selesai.

