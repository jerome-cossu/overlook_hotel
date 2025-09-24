package com.example.overlook_hotel.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.overlook_hotel.model.User;
import com.example.overlook_hotel.repository.UserRepository;
import com.example.overlook_hotel.service.UserService;
import com.example.overlook_hotel.dto.LoginRequest;
import com.example.overlook_hotel.dto.RegisterRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    // Injection via constructeur unique
    @Autowired
    public AuthController(UserRepository userRepository, UserService userService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @jakarta.validation.Valid RegisterRequest request) {
        User user = userService.register(request);
        return ResponseEntity.ok("Utilisateur créé : " + user.getEmail());
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", "Connexion réussie");
        response.put("email", user.getEmail());
        response.put("firstName", user.getFirstName());
        return ResponseEntity.ok(response);
    }
}
