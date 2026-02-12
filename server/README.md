# Server

This is the Spring Boot backend for Sec CERTIFICATE.

## Run locally

```bash
./mvnw spring-boot:run
```

The server runs on `http://localhost:8080` by default.

## Required env vars

- `DB_URL`
- `DB_USER`
- `DB_PASSWORD`

## Main endpoints

### Auth

- `POST /api/auth/login`
- `POST /api/auth/register`

### Templates

- `POST /api/templates` (create)
- `GET /api/templates` (list for logged-in user)

### Certificates

- `POST /api/certificates/generate` (queue PDF generation)
- `GET /api/certificates` (list user certificates)
- `GET /api/certificates/{id}/download` (download PDF when ready)

## Notes

- PDF generation runs in the background.
- Files are saved under `storage/certificates`.
