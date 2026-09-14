# KEYSTONE — Field Service Management Platform

> Zidio Development · Java Full-Stack Engineering Internship  
> Client: Meridian Facilities Management · Stack: Spring Boot 3 · React + TypeScript · PostgreSQL

## Overview

KEYSTONE is a full-stack field service management platform that digitises the entire maintenance workflow — from the moment a customer logs a request to the moment the job is closed and signed off. Dispatchers raise and assign work orders, technicians update jobs from the field, managers monitor SLA compliance, and customers track their own requests.

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Backend Framework | Spring Boot 3 |
| Security | Spring Security + JWT (stateless) |
| ORM | Hibernate / Spring Data JPA |
| Database | PostgreSQL |
| Migrations | Flyway |
| Frontend | React + TypeScript (Vite) |
| API Docs | springdoc-openapi (Swagger UI) |
| Build | Maven |

## Local Setup

### Prerequisites

- Java 21+
- Node.js 18+
- PostgreSQL 15+

### 1. Database

Create a PostgreSQL database:

```sql
CREATE DATABASE keystone_db;
```

### 2. Environment Variables

Set these before running the backend. Never commit real credentials or secrets.

| Variable | Example |
|---|---|
| SPRING_DATASOURCE_URL | jdbc:postgresql://localhost:5432/keystone_db |
| SPRING_DATASOURCE_USERNAME | postgres |
| SPRING_DATASOURCE_PASSWORD | yourpassword |
| JWT_SECRET | your-jwt-secret |
| JWT_EXPIRATION | 86400000 |
| MAIL_USERNAME | your@gmail.com |
| MAIL_PASSWORD | your-app-password |

### 3. Run Backend

From the project root:

```bash
./mvnw clean spring-boot:run
```

For Windows:

```powershell
.\mvnw.cmd clean spring-boot:run
```

Flyway migrations run automatically on startup.

Backend runs on:

```text
http://localhost:8080
```

### 4. Run Frontend

If the frontend project is available separately:

```bash
cd keystone-frontend
npm install
npm run dev
```

Frontend runs on:

```text
http://localhost:3000
```

## Demo Credentials

> These credentials are for internship demonstration purposes only.

| Role | Email | Password |
|---|---|---|
| Manager | admin@keystone.com | admin123 |
| Dispatcher | dispatcher@keystone.com | dispatch123 |
| Technician | raju@keystone.com | tech123 |

## API Documentation

### Local

- Swagger UI: http://localhost:8080/swagger-ui/index.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

### Live Deployment

- Backend API: https://keystone-backend-q27u.onrender.com
- Swagger UI: https://keystone-backend-q27u.onrender.com/swagger-ui/index.html
- OpenAPI JSON: https://keystone-backend-q27u.onrender.com/v3/api-docs

Click **Authorize** in Swagger UI and paste the JWT obtained from:

```text
POST /api/auth/login
```

Then test the protected endpoints.

## Architecture

```text
React SPA (Vite)
       |
Spring Boot Controllers
       |
Service Layer
       |
Spring Data JPA Repositories
       |
PostgreSQL + Flyway Migrations (V1-V4)
```

### Work-Order Lifecycle

```text
NEW -> ASSIGNED -> IN_PROGRESS <-> ON_HOLD
                       |
                  COMPLETED -> CLOSED (terminal)

NEW/ASSIGNED/IN_PROGRESS -> CANCELLED (terminal)
```

All transitions are enforced in the service layer. Illegal state transitions return HTTP 409.

Every transition writes an append-only `WorkOrderStatusHistory` record.

### Security Model

- Stateless JWT authentication
- BCrypt password hashing
- `@PreAuthorize` role checks on protected endpoints
- Customers can access only their own organisation's data

## Features

| Feature | Description |
|---|---|
| F1 | Auth and Roles — JWT login, 4 roles |
| F2 | Customers and Sites — CRUD, searchable lists |
| F3 | Work Orders — full lifecycle, server-side validation |
| F4 | Dispatch and Assignment — assign technician, Kanban board |
| F5 | Technician View — mobile-responsive, start/hold/complete |
| F6 | Parts and Time Logging — transactional stock decrement |
| F7 | SLA Tracking — priority-based due dates, breach scheduler |
| F8 | Dashboard and Reporting — status counts, SLA compliance |
| F9 | Customer Portal — raise requests, track own work orders |

## Deployment

| Component | URL |
|---|---|
| Backend API | https://keystone-backend-q27u.onrender.com |
| Swagger UI | https://keystone-backend-q27u.onrender.com/swagger-ui/index.html |
| OpenAPI JSON | https://keystone-backend-q27u.onrender.com/v3/api-docs |
| Frontend | Not deployed |

### Deployment Stack

- Backend: Render
- Database: Neon PostgreSQL
- Database Migrations: Flyway
- Containerisation: Docker
- Runtime: Java 21

## Project Status

The backend is deployed and running successfully on Render with PostgreSQL hosted on Neon. Flyway migrations are applied automatically during application startup.

Built for **Zidio Development Java Full-Stack Engineering Internship** — Project KEYSTONE v1.0