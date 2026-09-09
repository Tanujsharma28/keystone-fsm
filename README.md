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

    CREATE DATABASE keystone_db;

### 2. Environment Variables

Set these before running the backend (never commit real values):

| Variable | Example |
|---|---|
| SPRING_DATASOURCE_URL | jdbc:postgresql://localhost:5432/keystone_db |
| SPRING_DATASOURCE_USERNAME | postgres |
| SPRING_DATASOURCE_PASSWORD | yourpassword |
| JWT_SECRET | keystone-super-secret-key-32chars!! |
| JWT_EXPIRATION | 86400000 |
| MAIL_USERNAME | your@gmail.com |
| MAIL_PASSWORD | xxxx xxxx xxxx xxxx |

### 3. Run Backend

    cd backend
    ./mvnw clean spring-boot:run

Flyway migrations run automatically on startup.
Backend runs on: http://localhost:8080

### 4. Run Frontend

    cd keystone-frontend
    npm install
    npm run dev

Frontend runs on: http://localhost:3000

## Seed Login Credentials

| Role | Email | Password |
|---|---|---|
| Manager/Admin | admin@keystone.com | password123 |
| Dispatcher | dispatcher@keystone.com | password123 |
| Technician | tech1@keystone.com | password123 |
| Customer | customer@keystone.com | password123 |

## API Documentation

- Swagger UI: http://localhost:8080/swagger-ui/index.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

Click Authorize in Swagger UI, paste JWT from POST /api/auth/login, then test any endpoint.

## Architecture

    React SPA (Vite)
         |
    Spring Boot Controllers (JWT auth, DTO mapping)
         |
    Service Layer (state machine, SLA, transactions)
         |
    Spring Data JPA Repositories
         |
    PostgreSQL + Flyway Migrations (V1-V4)

### Work-Order Lifecycle

    NEW -> ASSIGNED -> IN_PROGRESS <-> ON_HOLD
                           |
                      COMPLETED -> CLOSED (terminal)
    NEW/ASSIGNED/IN_PROGRESS -> CANCELLED (terminal)

All transitions enforced in the service layer — illegal jumps return HTTP 409.
Every transition writes an append-only WorkOrderStatusHistory row.

### Security Model

- Stateless JWT — no server sessions
- BCrypt password hashing
- @PreAuthorize role checks on every protected endpoint
- Customers see only their own organisation data

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
| Backend API | TBD |
| Frontend | TBD |
| Swagger UI | TBD |

Built for Zidio Development Java Full-Stack Engineering Internship — Project KEYSTONE v1.0
