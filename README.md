# Sec CERTIFICATE

Sec CERTIFICATE is a system that helps a company create and manage certificate templates, then generate PDF certificates for customers.

## What it does

- Onboard customers
- Create certificate templates with placeholders
- Generate certificates from templates
- Download generated PDFs
- Health check endpoint for monitoring

## Run locally

### Backend

```bash
cd server
./mvnw spring-boot:run
```

### Frontend

```bash
cd client
npm install
npm run start
```

Open `http://localhost:4200`.

## Notes

- The backend expects DB settings in env vars: `DB_URL`, `DB_USER`, `DB_PASSWORD`.
- PDFs are stored under `server/storage/certificates`.
- Health check: `GET /api/health`
