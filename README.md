# OZIEL OS v1.0

**Proof of Work. Built in Java. Built in South Africa.**

OZIEL OS is Asenath Oziel Tyhaliti's personal proof and career operating system: a production-style Java full-stack platform for projects, build logs, skills, community impact and opportunity tracking.

## Stack

- Java 21, Spring Boot 3.4, Spring Security, JWT
- Spring Data JPA, PostgreSQL
- Maven, JUnit 5, Mockito
- React 18, Vite, TypeScript, Tailwind CSS
- TanStack Query, Axios, Recharts, Lucide

## Run locally

### 1. Backend

The default configuration runs with an in-memory H2 database for immediate local setup. PostgreSQL is supported through environment variables.

```bash
cd backend
./mvnw spring-boot:run
```

API: `http://localhost:8080`

Swagger: `http://localhost:8080/swagger-ui.html`

Admin: `admin@ozielos.co.za`

Default local password: `admin123`

### 2. Frontend

```bash
cd frontend
npm install
npm run dev
```

Open `http://localhost:5173`.

Copy `.env.example` files and set values as needed.

## PostgreSQL configuration

Set `DATABASE_URL`, `DATABASE_USERNAME` and `DATABASE_PASSWORD`. Supabase, Neon or local PostgreSQL can be used:

```bash
DATABASE_URL=jdbc:postgresql://host:5432/database
DATABASE_USERNAME=your-user
DATABASE_PASSWORD=your-password
JWT_SECRET=a-long-random-secret
ADMIN_EMAIL=admin@ozielos.co.za
ADMIN_PASSWORD=change-this-password
```

## New opportunity email alerts

When an admin adds a new listing from **Admin → Opportunity inbox**, OZIEL OS creates a durable email delivery record and sends it to the configured recipient. Failed sends are retried with backoff and appear in the admin delivery queue. Seeded starter listings do not generate alerts.

Email is enabled by default; SMS is disabled to avoid SMS costs and unnecessary traffic. The local `.env` is ignored by Git and already has the recipient address. To send mail through Gmail, enable 2-Step Verification and create an app password, then set it as `SMTP_PASSWORD` in `.env`. Never use or share your normal Google account password. Start the backend with:

```bash
cd backend
./run-local.sh
```

In deployments, configure `SMTP_HOST`, `SMTP_PORT`, `SMTP_USERNAME`, `SMTP_PASSWORD`, `NOTIFICATIONS_EMAIL_TO` and `NOTIFICATIONS_EMAIL_FROM` as environment variables. Keep the notification recipient and mail credentials out of source control. The system alerts on an opportunity when it is ingested into OZIEL OS; automatic detection of new company adverts requires integrating reliable feeds or APIs from those publishers.

For local Docker:

```bash
docker compose up --build
```

This starts PostgreSQL on port 5432 and the Java backend on port 8080.

## Frontend deployment

Deploy `frontend/` to Vercel. Set:

```text
VITE_API_BASE_URL=https://your-backend-host/api/v1
```

## Backend deployment

Deploy `backend/` to Render or Railway using the included Dockerfile. Configure `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD`, `JWT_SECRET`, `ADMIN_EMAIL` and `ADMIN_PASSWORD`.

Production should use HTTPS, rotate the admin password and generate a JWT secret of at least 32 bytes.
