ALTER TABLE app_user ADD COLUMN customer_id BIGINT NULL REFERENCES customer(id);
