package com.example.overlook_hotel.controller.auth;

import com.example.overlook_hotel.dto.auth.LoginRequest;
import com.example.overlook_hotel.dto.auth.AuthResponse;
import com.example.overlook_hotel.dto.auth.RegisterRequest;
import com.example.overlook_hotel.service.auth.AuthService;
import com.example.overlook_hotel.repository.auth.UserRepository;
import com.example.overlook_hotel.model.entity.User;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    public AuthController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest req) {
        
        String token = authService.register(req.getEmail(), req.getPassword(), req.getFirstName(), req.getLastName());
        // lookup user to get id (saved in register)
        User user = userRepository.findByEmail(req.getEmail()).orElseThrow();
        AuthResponse resp = new AuthResponse();
        resp.setToken(token);
        resp.setUserId(user.getId());
        resp.setEmail(req.getEmail());
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        
        String token = authService.login(req.getEmail(), req.getPassword());
        User user = userRepository.findByEmail(req.getEmail()).orElseThrow();
        AuthResponse resp = new AuthResponse();
        resp.setToken(token);
        resp.setUserId(user.getId());
        resp.setEmail(req.getEmail());
        return ResponseEntity.ok(resp);
    }
}
