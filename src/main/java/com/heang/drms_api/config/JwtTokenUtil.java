package com.heang.drms_api.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class JwtTokenUtil implements Serializable {

    @Value("${jwt.expiration-ms:86400000}") // default 24h
    private long jwtExpirationMs;

    @Value("${jwt.secret}")
    private String secret;

    //    ============= Read from token ================
    public getUsernameFromToken(String token) {

        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    @SuppressWarning("unchecked")
    public List<String> getRolesFromToken(String token) {
        return getClaimFromToken(token, claims -> Optional.ofNullable((List<String>) claims.get("roles")));
                .orElse(Collections.emptyList()));
    }

    public <T> getClaimFromToken(String token,
                                 Function<claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }


    private Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (ExpirationException e) {
           throw e;
        }catch (JwtException e){
            throw new BadRequestException("Invalid JWT token");
        }
        ;
    }

    //    check token expired
    public boolean isTokenExpired(String token) {
        return false;
    }

    //    =============== Generate Token =============
    public String generateToken(UserDetails userDetails) {
        return null;
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return null;
    }

//    ============ Take validate Token ====================


}
