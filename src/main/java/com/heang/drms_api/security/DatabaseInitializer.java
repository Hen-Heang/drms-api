package com.heang.drms_api.security;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class DatabaseInitializer {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void createOtpTablesIfMissing() {
        jdbcTemplate.execute("""
                CREATE TABLE IF NOT EXISTS tb_partner_otp (
                    id SERIAL PRIMARY KEY,
                    partner_account_id INTEGER NOT NULL REFERENCES tb_partner_account (id) ON DELETE CASCADE,
                    otp_code INTEGER NOT NULL,
                    partner_email VARCHAR(255) NOT NULL,
                    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                );
                """);

        jdbcTemplate.execute("""
                CREATE TABLE IF NOT EXISTS tb_merchant_otp (
                    id SERIAL PRIMARY KEY,
                   merchant_account_id INTEGER NOT NULL REFERENCES tb_merchant_account (id) ON DELETE CASCADE,
                    otp_code INTEGER NOT NULL,
                   merchant_email VARCHAR(255) NOT NULL,
                    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                );
                """);

    }
}