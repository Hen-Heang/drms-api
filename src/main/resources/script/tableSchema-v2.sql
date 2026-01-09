-- =========================================================
-- DRMS (Distributor/Retailer) Updated Schema for JWT + MyBatis
-- Roles: PARTNER, MERCHANT, ADMIN
-- =========================================================

-- =========================
-- 1) LOOKUP TABLES
-- =========================

-- Role table: controls authorization (PARTNER/MERCHANT/ADMIN)
CREATE TABLE IF NOT EXISTS tb_role (
                                       id   SERIAL PRIMARY KEY,
                                       name VARCHAR(50) NOT NULL UNIQUE
    );

-- Order status table: track order workflow
CREATE TABLE IF NOT EXISTS tb_order_status (
                                               id   SERIAL PRIMARY KEY,
                                               name VARCHAR(50) NOT NULL UNIQUE
    );

-- Notification types: templates, categories of notifications
CREATE TABLE IF NOT EXISTS tb_notification_type (
                                                    id       SERIAL PRIMARY KEY,
                                                    name     VARCHAR(100) NOT NULL UNIQUE,
    template TEXT
    );

-- Category: used for store categories and product categories
CREATE TABLE IF NOT EXISTS tb_category (
                                           id          SERIAL PRIMARY KEY,
                                           name        VARCHAR(150) NOT NULL UNIQUE,
    image       TEXT,
    is_active   BOOLEAN DEFAULT TRUE,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Product master: base product name (like "Coca Cola 330ml")
CREATE TABLE IF NOT EXISTS tb_product (
                                          id          SERIAL PRIMARY KEY,
                                          name        VARCHAR(255) NOT NULL UNIQUE,
    is_active   BOOLEAN DEFAULT TRUE,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Many-to-many mapping product <-> category
CREATE TABLE IF NOT EXISTS tb_product_category (
                                                   id          SERIAL PRIMARY KEY,
                                                   category_id INTEGER NOT NULL REFERENCES tb_category(id) ON DELETE CASCADE,
    product_id  INTEGER NOT NULL REFERENCES tb_product(id)  ON DELETE CASCADE,
    UNIQUE(category_id, product_id)
    );

-- =========================
-- 2) AUTH USERS (JWT CORE)
-- =========================

-- tb_user_account = ONLY identity table used for login/register/JWT
-- This replaces tb_distributor_account + tb_retailer_account
CREATE TABLE IF NOT EXISTS tb_user_account (
                                               id           BIGSERIAL PRIMARY KEY,
                                               role_id      INTEGER NOT NULL REFERENCES tb_role(id),

    email        VARCHAR(255) NOT NULL UNIQUE,
    password     TEXT NOT NULL,                 -- store bcrypt hash only
    full_name    VARCHAR(150) NOT NULL,
    phone        VARCHAR(50),

    is_verified  BOOLEAN DEFAULT FALSE,         -- email/otp verified or not
    is_active    BOOLEAN DEFAULT TRUE,          -- soft disable account

    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- =========================
-- 3) OPTIONAL PROFILE TABLES
-- =========================
-- Why profiles?
-- - Keep auth table clean (login fields only)
-- - Partner & Merchant can have different fields later

CREATE TABLE IF NOT EXISTS tb_partner_profile (
                                                  id           BIGSERIAL PRIMARY KEY,
                                                  user_id      BIGINT NOT NULL UNIQUE REFERENCES tb_user_account(id) ON DELETE CASCADE,

    company_name VARCHAR(255),
    gender       VARCHAR(20),
    address      TEXT,
    profile_image TEXT,

    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS tb_merchant_profile (
                                                   id           BIGSERIAL PRIMARY KEY,
                                                   user_id      BIGINT NOT NULL UNIQUE REFERENCES tb_user_account(id) ON DELETE CASCADE,

    shop_name    VARCHAR(255),
    gender       VARCHAR(20),
    address      TEXT,
    profile_image TEXT,

    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- =========================
-- 4) STORE & CATALOG (PARTNER SIDE)
-- =========================

-- Store belongs to a partner (owner_user_id must be PARTNER role in logic)
CREATE TABLE IF NOT EXISTS tb_store (
                                        id            BIGSERIAL PRIMARY KEY,
                                        owner_user_id BIGINT NOT NULL REFERENCES tb_user_account(id) ON DELETE CASCADE,

    name          VARCHAR(255) NOT NULL,
    banner_image  TEXT,
    description   TEXT,
    address       TEXT,

    is_publish    BOOLEAN DEFAULT TRUE,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS tb_store_phone (
                                              id       BIGSERIAL PRIMARY KEY,
                                              store_id BIGINT NOT NULL REFERENCES tb_store(id) ON DELETE CASCADE,
    phone    VARCHAR(50) NOT NULL
    );

-- Store can have multiple categories
CREATE TABLE IF NOT EXISTS tb_store_category (
                                                 id          BIGSERIAL PRIMARY KEY,
                                                 store_id    BIGINT NOT NULL REFERENCES tb_store(id) ON DELETE CASCADE,
    category_id INTEGER NOT NULL REFERENCES tb_category(id) ON DELETE CASCADE,
    UNIQUE(store_id, category_id)
    );

-- Store product detail = store's inventory + price + publish status
CREATE TABLE IF NOT EXISTS tb_store_product_detail (
                                                       id           BIGSERIAL PRIMARY KEY,
                                                       store_id     BIGINT NOT NULL REFERENCES tb_store(id) ON DELETE CASCADE,
    product_id   INTEGER NOT NULL REFERENCES tb_product(id) ON DELETE CASCADE,

    qty          INTEGER DEFAULT 0,
    price        DOUBLE PRECISION DEFAULT 0,

    is_publish   BOOLEAN DEFAULT FALSE,
    image        TEXT,
    category_id  INTEGER REFERENCES tb_category(id),
    description  TEXT,

    is_active    BOOLEAN DEFAULT TRUE,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    UNIQUE(store_id, product_id)
    );

-- =========================
-- 5) PRODUCT IMPORTS (STOCK IN)
-- =========================

CREATE TABLE IF NOT EXISTS tb_product_import (
                                                 id         BIGSERIAL PRIMARY KEY,
                                                 store_id   BIGINT NOT NULL REFERENCES tb_store(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS tb_product_import_detail (
                                                        id                BIGSERIAL PRIMARY KEY,
                                                        product_import_id BIGINT NOT NULL REFERENCES tb_product_import(id) ON DELETE CASCADE,
    product_id        INTEGER NOT NULL REFERENCES tb_product(id) ON DELETE CASCADE,

    qty               INTEGER NOT NULL,
    price             DOUBLE PRECISION NOT NULL,
    category_id       INTEGER REFERENCES tb_category(id)
    );

-- =========================
-- 6) ORDERS (MERCHANT SIDE)
-- =========================

-- Order belongs to merchant user
CREATE TABLE IF NOT EXISTS tb_order (
                                        id            BIGSERIAL PRIMARY KEY,

                                        buyer_user_id BIGINT NOT NULL REFERENCES tb_user_account(id) ON DELETE CASCADE,
    store_id      BIGINT NOT NULL REFERENCES tb_store(id) ON DELETE CASCADE,
    status_id     INTEGER NOT NULL REFERENCES tb_order_status(id),

    total_price   DOUBLE PRECISION DEFAULT 0,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS tb_order_detail (
                                               id               BIGSERIAL PRIMARY KEY,
                                               order_id         BIGINT NOT NULL REFERENCES tb_order(id) ON DELETE CASCADE,

    store_product_id BIGINT NOT NULL REFERENCES tb_store_product_detail(id),
    qty              INTEGER NOT NULL,
    unit_price       DOUBLE PRECISION NOT NULL
    );

-- =========================
-- 7) RATINGS & BOOKMARKS
-- =========================

CREATE TABLE IF NOT EXISTS tb_rating_detail (
                                                id         BIGSERIAL PRIMARY KEY,
                                                store_id   BIGINT NOT NULL REFERENCES tb_store(id) ON DELETE CASCADE,
    user_id    BIGINT NOT NULL REFERENCES tb_user_account(id) ON DELETE CASCADE,

    rated_star INTEGER CHECK (rated_star BETWEEN 1 AND 5),
    comment    TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    UNIQUE(store_id, user_id)
    );

CREATE TABLE IF NOT EXISTS tb_bookmark (
                                           id         BIGSERIAL PRIMARY KEY,
                                           store_id   BIGINT NOT NULL REFERENCES tb_store(id) ON DELETE CASCADE,
    user_id    BIGINT NOT NULL REFERENCES tb_user_account(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    UNIQUE(store_id, user_id)
    );

-- =========================
-- 8) NOTIFICATIONS (UNIFIED)
-- =========================

CREATE TABLE IF NOT EXISTS tb_notification (
                                               id         BIGSERIAL PRIMARY KEY,
                                               user_id    BIGINT NOT NULL REFERENCES tb_user_account(id) ON DELETE CASCADE,
    type_id    INTEGER NOT NULL REFERENCES tb_notification_type(id),

    content    TEXT,
    is_read    BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- =========================
-- 9) OTP (UNIFIED)
-- =========================
-- Use for: REGISTER_VERIFY, RESET_PASSWORD, LOGIN_2FA (future)

CREATE TABLE IF NOT EXISTS tb_otp (
                                      id         BIGSERIAL PRIMARY KEY,
                                      user_id    BIGINT REFERENCES tb_user_account(id) ON DELETE CASCADE,

    email      VARCHAR(255) NOT NULL,
    otp_code   VARCHAR(10) NOT NULL,
    purpose    VARCHAR(30) NOT NULL,   -- REGISTER, RESET_PASSWORD
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- =========================
-- 10) REFRESH TOKEN (FOR LOGIN)
-- =========================

CREATE TABLE IF NOT EXISTS tb_refresh_token (
                                                id         BIGSERIAL PRIMARY KEY,
                                                user_id    BIGINT NOT NULL REFERENCES tb_user_account(id) ON DELETE CASCADE,

    token      TEXT NOT NULL UNIQUE,
    expires_at TIMESTAMP NOT NULL,
    revoked    BOOLEAN DEFAULT FALSE,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- =========================================================
-- 11) SEED BASIC DATA
-- =========================================================

-- Roles
INSERT INTO tb_role (id, name) VALUES (1, 'PARTNER')  ON CONFLICT (id) DO NOTHING;
INSERT INTO tb_role (id, name) VALUES (2, 'MERCHANT') ON CONFLICT (id) DO NOTHING;
INSERT INTO tb_role (id, name) VALUES (3, 'ADMIN')    ON CONFLICT (id) DO NOTHING;

-- Order status
INSERT INTO tb_order_status (id, name) VALUES
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
