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

    @Autowired
    public AuthController(UserRepository userRepository, UserService userService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @jakarta.validation.Valid RegisterRequest request) {
        // Call the service layer to handle registration logic
        User user = userService.register(request);
        return ResponseEntity.ok("User created: " + user.getEmail());
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        // Retrieve user by email, throw an exception if not found
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if the provided password matches the stored hashed password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Incorrect Password");
        }

        // Prepare response with user info (without sending password)
        Map<String, String> response = new HashMap<>();
        response.put("message", "login successful");
        response.put("email", user.getEmail());
        response.put("lastName", user.getLastName());
        response.put("firstName", user.getFirstName());
        response.put("roleId", String.valueOf(user.getRole().getId()));
        response.put("roleName", user.getRole().getName());

        return ResponseEntity.ok(response);
    }
}
