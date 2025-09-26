package com.example.overlook_hotel.controller.auth;

import com.example.overlook_hotel.dto.auth.LoginRequest;
import com.example.overlook_hotel.dto.auth.AuthResponse;
import com.example.overlook_hotel.dto.auth.RegisterRequest;
import com.example.overlook_hotel.service.auth.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest req) {
        String token = authService.register(req.getEmail(), req.getPassword(), req.getFirstName(), req.getLastName());
        AuthResponse resp = new AuthResponse();
        resp.setToken(token);
        resp.setUserId(null);
        resp.setEmail(req.getEmail());
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        String token = authService.login(req.getEmail(), req.getPassword());
        AuthResponse resp = new AuthResponse();
        resp.setToken(token);
        resp.setEmail(req.getEmail());
        return ResponseEntity.ok(resp);
    }
}
