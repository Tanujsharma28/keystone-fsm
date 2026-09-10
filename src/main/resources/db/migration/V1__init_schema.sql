-- V1__init_schema.sql
-- KEYSTONE initial schema

-- ENUM types
CREATE TYPE user_role AS ENUM (
    'DISPATCHER',
    'TECHNICIAN',
    'MANAGER',
    'CUSTOMER'
);

CREATE TYPE work_order_status AS ENUM (
    'NEW',
    'ASSIGNED',
    'IN_PROGRESS',
    'ON_HOLD',
    'COMPLETED',
    'CLOSED',
    'CANCELLED'
);

CREATE TYPE priority_level AS ENUM (
    'LOW',
    'MEDIUM',
    'HIGH',
    'CRITICAL'
);

-- CUSTOMER table
CREATE TABLE IF NOT EXISTS customer (
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    contact_email VARCHAR(255) NOT NULL UNIQUE,
    created_at    TIMESTAMP NOT NULL DEFAULT NOW()
);

-- SITE table
CREATE TABLE IF NOT EXISTS site (
    id          BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL REFERENCES customer(id),
    name        VARCHAR(255) NOT NULL,
    address     TEXT NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT NOW()
);

-- USER table
CREATE TABLE IF NOT EXISTS app_user (
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    email         VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role          user_role NOT NULL,
    customer_id   BIGINT REFERENCES customer(id),
    created_at    TIMESTAMP NOT NULL DEFAULT NOW()
);

-- PART table
CREATE TABLE IF NOT EXISTS part (
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    sku        VARCHAR(100) NOT NULL UNIQUE,
    unit_cost  NUMERIC(10,2) NOT NULL,
    stock_qty  INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- WORK ORDER table
CREATE TABLE IF NOT EXISTS work_order (
    id          BIGSERIAL PRIMARY KEY,
    code        VARCHAR(50) NOT NULL UNIQUE,
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    priority    priority_level NOT NULL DEFAULT 'MEDIUM',
    status      work_order_status NOT NULL DEFAULT 'NEW',
    customer_id BIGINT NOT NULL REFERENCES customer(id),
    site_id     BIGINT NOT NULL REFERENCES site(id),
    assigned_to BIGINT REFERENCES app_user(id),
    sla_due_at  TIMESTAMP,
    created_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP NOT NULL DEFAULT NOW()
);

-- WORK ORDER STATUS HISTORY table (append-only)
CREATE TABLE IF NOT EXISTS work_order_status_history (
    id            BIGSERIAL PRIMARY KEY,
    work_order_id BIGINT NOT NULL REFERENCES work_order(id),
    from_status   work_order_status,
    to_status     work_order_status NOT NULL,
    changed_by    BIGINT REFERENCES app_user(id),
    note          TEXT,
    changed_at    TIMESTAMP NOT NULL DEFAULT NOW()
);

-- PART USAGE table
CREATE TABLE IF NOT EXISTS part_usage (
    id            BIGSERIAL PRIMARY KEY,
    work_order_id BIGINT NOT NULL REFERENCES work_order(id),
    part_id       BIGINT NOT NULL REFERENCES part(id),
    qty_used      INTEGER NOT NULL,
    logged_at     TIMESTAMP NOT NULL DEFAULT NOW()
);

-- TIME LOG table
CREATE TABLE IF NOT EXISTS time_log (
    id            BIGSERIAL PRIMARY KEY,
    work_order_id BIGINT NOT NULL REFERENCES work_order(id),
    technician_id BIGINT NOT NULL REFERENCES app_user(id),
    minutes       INTEGER NOT NULL,
    note          TEXT,
    logged_at     TIMESTAMP NOT NULL DEFAULT NOW()
);

-- INDEXES
CREATE INDEX idx_work_order_status   ON work_order(status);
CREATE INDEX idx_work_order_customer ON work_order(customer_id);
CREATE INDEX idx_work_order_assigned ON work_order(assigned_to);
CREATE INDEX idx_site_customer       ON site(customer_id);
CREATE INDEX idx_part_usage_wo       ON part_usage(work_order_id);
CREATE INDEX idx_time_log_wo         ON time_log(work_order_id);
