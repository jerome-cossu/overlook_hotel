package com.example.overlook_hotel.service.auth;

import com.example.overlook_hotel.repository.auth.UserRepository;
import com.example.overlook_hotel.repository.auth.RoleRepository;
import com.example.overlook_hotel.model.entity.User;
import com.example.overlook_hotel.model.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Transactional
    public String register(String email, String rawPassword, String firstName, String lastName) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use");
        }
        Role role = roleRepository.findByName("GUEST")
                .orElseThrow(() -> new IllegalStateException("Default role GUEST not found"));

        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(role);
        userRepository.save(user);

        return jwtService.generateToken(user.getEmail());
    }

    public String login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return jwtService.generateToken(user.getEmail());
    }
}
