CREATE TABLE tb_user_account (
                                 id              SERIAL PRIMARY KEY,
                                 role_id         INTEGER NOT NULL REFERENCES tb_role(id),
                                 email           VARCHAR(255) NOT NULL UNIQUE,
                                 password        TEXT NOT NULL,              -- bcrypt hash
                                 full_name       VARCHAR(255),
                                 phone           VARCHAR(50),
                                 is_verified     BOOLEAN DEFAULT FALSE,
                                 is_active       BOOLEAN DEFAULT TRUE,
                                 created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ----------------------------------------------------------------------
CREATE TABLE tb_role (
                         id   SERIAL PRIMARY KEY,
                         name VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO tb_role (id, name) VALUES
                                   (1, 'PARTNER'),
                                   (2, 'MERCHANT')
    ON CONFLICT DO NOTHING;

--

CREATE TABLE tb_refresh_token (
                                  id           SERIAL PRIMARY KEY,
                                  user_id      INTEGER NOT NULL REFERENCES tb_user_account(id) ON DELETE CASCADE,
                                  token        TEXT NOT NULL UNIQUE,
                                  expires_at   TIMESTAMP NOT NULL,
                                  revoked      BOOLEAN DEFAULT FALSE,
                                  created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

--

CREATE TABLE tb_otp (
                        id          SERIAL PRIMARY KEY,
                        user_id     INTEGER NOT NULL REFERENCES tb_user_account(id) ON DELETE CASCADE,
                        otp_code    VARCHAR(10) NOT NULL,
                        purpose     VARCHAR(50),   -- REGISTER, RESET_PASSWORD
                        expires_at  TIMESTAMP NOT NULL,
                        used        BOOLEAN DEFAULT FALSE,
                        created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

--


