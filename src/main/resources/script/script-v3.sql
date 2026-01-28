-- ==========================================
-- DRMS - Distributor/Retailer Full Schema (PG)
-- (Converted from Partner/Merchant)
-- ==========================================

BEGIN;

-- =========================
-- 0) updated_date trigger
-- =========================
CREATE OR REPLACE FUNCTION set_updated_date()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_date = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- =========================
-- 1) Core reference tables
-- =========================
CREATE TABLE IF NOT EXISTS tb_role (
                                       id   BIGSERIAL PRIMARY KEY,
                                       name VARCHAR(50) NOT NULL UNIQUE
    );

CREATE TABLE IF NOT EXISTS tb_status (
                                         id   BIGSERIAL PRIMARY KEY,
                                         name VARCHAR(50) NOT NULL UNIQUE
    );

CREATE TABLE IF NOT EXISTS tb_category (
                                           id           BIGSERIAL PRIMARY KEY,
                                           name         VARCHAR(150) NOT NULL UNIQUE,
    image        TEXT,
    is_active    BOOLEAN DEFAULT TRUE,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS tb_product (
                                          id           BIGSERIAL PRIMARY KEY,
                                          name         VARCHAR(255) NOT NULL UNIQUE,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active    BOOLEAN DEFAULT TRUE
    );

CREATE TABLE IF NOT EXISTS tb_product_category (
                                                   id          BIGSERIAL PRIMARY KEY,
                                                   category_id BIGINT NOT NULL REFERENCES tb_category(id) ON DELETE CASCADE,
    product_id  BIGINT NOT NULL REFERENCES tb_product(id)  ON DELETE CASCADE,
    CONSTRAINT uq_product_category UNIQUE (category_id, product_id)
    );

-- =========================
-- 2) Accounts and profiles
-- =========================
CREATE TABLE IF NOT EXISTS tb_distributor_account (
                                                      id           BIGSERIAL PRIMARY KEY,
                                                      role_id      BIGINT NOT NULL REFERENCES tb_role(id),
    email        VARCHAR(255) NOT NULL UNIQUE,
    password     TEXT NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_verified  BOOLEAN DEFAULT FALSE,
    is_active    BOOLEAN DEFAULT TRUE
    );

CREATE TABLE IF NOT EXISTS tb_retailer_account (
                                                   id           BIGSERIAL PRIMARY KEY,
                                                   role_id      BIGINT NOT NULL REFERENCES tb_role(id),
    email        VARCHAR(255) NOT NULL UNIQUE,
    password     TEXT NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_verified  BOOLEAN DEFAULT FALSE,
    is_active    BOOLEAN DEFAULT TRUE
    );

CREATE TABLE IF NOT EXISTS tb_distributor_info (
                                                   id                   BIGSERIAL PRIMARY KEY,
                                                   distributor_account_id BIGINT NOT NULL REFERENCES tb_distributor_account(id) ON DELETE CASCADE,
    first_name           VARCHAR(150),
    last_name            VARCHAR(150),
    gender               VARCHAR(20),
    profile_image        TEXT,
    primary_phone_number VARCHAR(50),
    created_date         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_distributor_info_account UNIQUE (distributor_account_id)
    );

CREATE TABLE IF NOT EXISTS tb_retailer_info (
                                                id                   BIGSERIAL PRIMARY KEY,
                                                retailer_account_id  BIGINT NOT NULL REFERENCES tb_retailer_account(id) ON DELETE CASCADE,
    first_name           VARCHAR(150),
    last_name            VARCHAR(150),
    gender               VARCHAR(20),
    address              TEXT,
    primary_phone_number VARCHAR(50),
    profile_image        TEXT,
    created_date         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_retailer_info_account UNIQUE (retailer_account_id)
    );

CREATE TABLE IF NOT EXISTS tb_distributor_phone (
                                                    id                 BIGSERIAL PRIMARY KEY,
                                                    distributor_info_id BIGINT NOT NULL REFERENCES tb_distributor_info(id) ON DELETE CASCADE,
    phone_number       VARCHAR(50) NOT NULL
    );

CREATE TABLE IF NOT EXISTS tb_retailer_phone (
                                                 id               BIGSERIAL PRIMARY KEY,
                                                 retailer_info_id BIGINT NOT NULL REFERENCES tb_retailer_info(id) ON DELETE CASCADE,
    phone_number     VARCHAR(50) NOT NULL
    );

-- =========================
-- 3) Stores and catalog
-- =========================
CREATE TABLE IF NOT EXISTS tb_store (
                                        id                    BIGSERIAL PRIMARY KEY,
                                        distributor_account_id BIGINT NOT NULL REFERENCES tb_distributor_account(id) ON DELETE CASCADE,
    name                  VARCHAR(255) NOT NULL,
    banner_image          TEXT,
    description           TEXT,
    address               TEXT,
    is_publish            BOOLEAN DEFAULT TRUE,
    created_date          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date          TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS tb_store_phone (
                                              id       BIGSERIAL PRIMARY KEY,
                                              store_id BIGINT NOT NULL REFERENCES tb_store(id) ON DELETE CASCADE,
    phone    VARCHAR(50) NOT NULL
    );

CREATE TABLE IF NOT EXISTS tb_store_category (
                                                 id          BIGSERIAL PRIMARY KEY,
                                                 store_id    BIGINT NOT NULL REFERENCES tb_store(id) ON DELETE CASCADE,
    category_id BIGINT NOT NULL REFERENCES tb_category(id) ON DELETE CASCADE,
    CONSTRAINT uq_store_category UNIQUE (store_id, category_id)
    );

CREATE TABLE IF NOT EXISTS tb_store_product_detail (
                                                       id           BIGSERIAL PRIMARY KEY,
                                                       store_id     BIGINT NOT NULL REFERENCES tb_store(id) ON DELETE CASCADE,
    product_id   BIGINT NOT NULL REFERENCES tb_product(id) ON DELETE CASCADE,
    qty          INTEGER DEFAULT 0,
    price        DOUBLE PRECISION DEFAULT 0,
    is_publish   BOOLEAN DEFAULT FALSE,
    image        TEXT,
    category_id  BIGINT REFERENCES tb_category(id),
    description  TEXT,
    is_active    BOOLEAN DEFAULT TRUE,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_store_product UNIQUE (store_id, product_id)
    );

-- =========================
-- 4) Imports
-- =========================
CREATE TABLE IF NOT EXISTS tb_product_import (
                                                 id           BIGSERIAL PRIMARY KEY,
                                                 created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                 store_id     BIGINT NOT NULL REFERENCES tb_store(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS tb_product_import_detail (
                                                        id                BIGSERIAL PRIMARY KEY,
                                                        product_id        BIGINT NOT NULL REFERENCES tb_product(id) ON DELETE CASCADE,
    product_import_id BIGINT NOT NULL REFERENCES tb_product_import(id) ON DELETE CASCADE,
    qty               INTEGER NOT NULL,
    price             DOUBLE PRECISION NOT NULL,
    category_id       BIGINT REFERENCES tb_category(id)
    );

-- =========================
-- 5) Orders
-- =========================
CREATE TABLE IF NOT EXISTS tb_order (
                                        id                  BIGSERIAL PRIMARY KEY,
                                        retailer_account_id BIGINT NOT NULL REFERENCES tb_retailer_account(id) ON DELETE CASCADE,
    store_id            BIGINT NOT NULL REFERENCES tb_store(id) ON DELETE CASCADE,
    status_id           BIGINT NOT NULL REFERENCES tb_status(id),
    created_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_price         DOUBLE PRECISION DEFAULT 0
    );

CREATE TABLE IF NOT EXISTS tb_order_detail (
                                               id               BIGSERIAL PRIMARY KEY,
                                               order_id         BIGINT NOT NULL REFERENCES tb_order(id) ON DELETE CASCADE,
    qty              INTEGER NOT NULL,
    unit_price       DOUBLE PRECISION NOT NULL,
    store_product_id BIGINT NOT NULL REFERENCES tb_store_product_detail(id)
    );

-- =========================
-- 6) Ratings and bookmarks
-- =========================
CREATE TABLE IF NOT EXISTS tb_rating_detail (
                                                id                  BIGSERIAL PRIMARY KEY,
                                                store_id            BIGINT NOT NULL REFERENCES tb_store(id) ON DELETE CASCADE,
    retailer_account_id BIGINT NOT NULL REFERENCES tb_retailer_account(id) ON DELETE CASCADE,
    rated_star          INTEGER CHECK (rated_star BETWEEN 1 AND 5),
    comment             TEXT,
    created_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_rating_store_retailer UNIQUE (store_id, retailer_account_id)
    );

CREATE TABLE IF NOT EXISTS tb_bookmark (
                                           id                  BIGSERIAL PRIMARY KEY,
                                           store_id            BIGINT NOT NULL REFERENCES tb_store(id) ON DELETE CASCADE,
    retailer_account_id BIGINT NOT NULL REFERENCES tb_retailer_account(id) ON DELETE CASCADE,
    created_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_bookmark_store_retailer UNIQUE (store_id, retailer_account_id)
    );

-- =========================
-- 7) Notifications
-- =========================
CREATE TABLE IF NOT EXISTS tb_notification_type (
                                                    id       BIGSERIAL PRIMARY KEY,
                                                    name     VARCHAR(100) NOT NULL UNIQUE,
    template TEXT
    );

CREATE TABLE IF NOT EXISTS tb_distributor_notification (
                                                           id                    BIGSERIAL PRIMARY KEY,
                                                           distributor_account_id BIGINT NOT NULL REFERENCES tb_distributor_account(id) ON DELETE CASCADE,
    type_id               BIGINT NOT NULL REFERENCES tb_notification_type(id),
    content               TEXT,
    is_read               BOOLEAN DEFAULT FALSE,
    created_date          TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS tb_retailer_notification (
                                                        id                  BIGSERIAL PRIMARY KEY,
                                                        retailer_account_id BIGINT NOT NULL REFERENCES tb_retailer_account(id) ON DELETE CASCADE,
    type_id             BIGINT NOT NULL REFERENCES tb_notification_type(id),
    content             TEXT,
    is_read             BOOLEAN DEFAULT FALSE,
    created_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- =========================
-- 8) OTPs
-- =========================
CREATE TABLE IF NOT EXISTS tb_distributor_otp (
                                                  id                    BIGSERIAL PRIMARY KEY,
                                                  distributor_account_id BIGINT NOT NULL REFERENCES tb_distributor_account(id) ON DELETE CASCADE,
    otp_code              INTEGER NOT NULL,
    distributor_email     VARCHAR(255) NOT NULL,
    created_date          TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS tb_retailer_otp (
                                               id                  BIGSERIAL PRIMARY KEY,
                                               retailer_account_id BIGINT NOT NULL REFERENCES tb_retailer_account(id) ON DELETE CASCADE,
    otp_code            INTEGER NOT NULL,
    retailer_email      VARCHAR(255) NOT NULL,
    created_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- =========================
-- 9) Auto-update updated_date triggers
-- =========================
DO $$
DECLARE
r RECORD;
BEGIN
FOR r IN
SELECT table_name
FROM information_schema.columns
WHERE column_name = 'updated_date'
  AND table_schema = 'public'
    LOOP
    EXECUTE format('DROP TRIGGER IF EXISTS trg_%I_updated_date ON %I;', r.table_name, r.table_name);
EXECUTE format('CREATE TRIGGER trg_%I_updated_date
                    BEFORE UPDATE ON %I
                    FOR EACH ROW
                    EXECUTE FUNCTION set_updated_date();', r.table_name, r.table_name);
END LOOP;
END $$;

-- =========================
-- 10) Seed basic lookup data
-- =========================
INSERT INTO tb_role (id, name)
VALUES (1, 'DISTRIBUTOR')
    ON CONFLICT (id) DO NOTHING;

INSERT INTO tb_role (id, name)
VALUES (2, 'RETAILER')
    ON CONFLICT (id) DO NOTHING;

INSERT INTO tb_status (id, name)
VALUES
    (1, 'PENDING'),
    (2, 'PROCESSING'),
    (3, 'CONFIRMED'),
    (4, 'SHIPPING'),
    (5, 'DELIVERED'),
    (6, 'COMPLETED'),
    (7, 'CANCELLED'),
    (8, 'REJECTED'),
    (9, 'DRAFT')
    ON CONFLICT (id) DO NOTHING;

COMMIT;
