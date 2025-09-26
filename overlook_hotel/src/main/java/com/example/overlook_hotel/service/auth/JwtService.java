package com.example.overlook_hotel.service.auth;

import com.example.overlook_hotel.config.JwtUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class JwtService {
    @Autowired
    private JwtUtils jwtUtils;

    public String generateToken(String username) {
        return jwtUtils.generateToken(username);
    }

    public boolean validateToken(String token) {
        return jwtUtils.validate(token);
    }

    public String getUsernameFromToken(String token) {
        return jwtUtils.getUsername(token);
    }
}
