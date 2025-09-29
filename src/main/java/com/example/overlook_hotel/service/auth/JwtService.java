package com.example.overlook_hotel.service.auth;

import com.example.overlook_hotel.config.CustomUserPrincipal;
import com.example.overlook_hotel.config.JwtUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class JwtService {

    private JwtUtils jwtUtils;

    public JwtService(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    public String generateToken(CustomUserPrincipal principal) {
    return jwtUtils.generateTokenWithClaims(principal.getUsername(),
        Map.of("uid", String.valueOf(principal.getId())));
    }

    public boolean validateToken(String token) { 
        return jwtUtils.validate(token); 
    }

    public String getUsernameFromToken(String token) { 
        return jwtUtils.getUsername(token); 
    }

    public Long getUserIdFromToken(String token) {
        String uid = jwtUtils.getClaim(token, "uid");
        return uid != null ? Long.valueOf(uid) : null;
    }
}
