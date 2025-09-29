package com.example.overlook_hotel.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtils {

    @Value("${jwt.secret:change_this_to_a_strong_secret}")
    private String jwtSecret;

    @Value("${jwt.expiration-ms:86400000}") // 1 day default
    private long jwtExpirationMs;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken(String username) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + jwtExpirationMs);
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(now)
            .setExpiration(exp)
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    public String generateTokenWithClaims(String username, Map<String, String> claimsMap) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + jwtExpirationMs);
        var builder = Jwts.builder().setSubject(username).setIssuedAt(now).setExpiration(exp);
        claimsMap.forEach(builder::claim);
        return builder.signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    public boolean validate(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException ex) {
            return false;
        }
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public String getClaim(String token, String name) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
                    .parseClaimsJws(token).getBody();
            Object v = claims.get(name);
            return v != null ? v.toString() : null;
        } catch (JwtException ex) {
            return null;
        }
    }
}
