package com.example.overlook_hotel.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    @Value("${app.jwt.secret:${JWT_SECRET:change_this_secret}}")
    private String jwtSecret;

    @Value("${app.jwt.exp-ms:${JWT_EXP_MS:3600000}}") // default 1h
    private long jwtExpMs;

    private Key key;

    @PostConstruct
    public void init() {
        // Use HMAC key derived from secret; for production ensure secret length is sufficient
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken(String subject, Collection<String> roles) {
        long now = System.currentTimeMillis();
        JwtBuilder b = Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + jwtExpMs))
                .signWith(key, SignatureAlgorithm.HS256);

        if (roles != null && !roles.isEmpty()) {
            b.claim("roles", roles);
        }
        return b.compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String getSubject(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> getRoles(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        Object raw = claims.get("roles");
        if (raw instanceof List) {
            return ((List<?>) raw).stream().map(Object::toString).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public Date getExpiration(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.getExpiration();
    }
}
