package com.heang.drms_api.security;

import io.jsonwebtoken.Jwts;

import java.util.Base64;

public class JwtGenerateKey {
    public static void main(String[] args) {
        byte[] key = Jwts.SIG.HS256.key().build().getEncoded(); // 256-bit
        System.out.println(Base64.getEncoder().encodeToString(key));
    }


}
