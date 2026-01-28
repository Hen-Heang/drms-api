package com.heang.drms_api.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Base64;

public class JwtGenerateKey {

    public static void main(String[] args) {

        // ✅ Generate a secure 512-bit key for HS512
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

        // Encode to Base64 for storing in env / yml
        String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());

        System.out.println("JWT_SECRET=");
        System.out.println(base64Key);
        System.out.println("bytes=" + key.getEncoded().length);
    }
}
