package com.example.overlook_hotel.service.auth;

import com.example.overlook_hotel.repository.auth.UserRepository;
import com.example.overlook_hotel.repository.auth.RoleRepository;
import com.example.overlook_hotel.model.entity.User;
import com.example.overlook_hotel.model.entity.Role;
import com.example.overlook_hotel.model.enums.RoleName;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.example.overlook_hotel.config.CustomUserPrincipal;

@Service
public class AuthService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public String register(String email, String rawPassword, String firstName, String lastName) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use");
        }
        Role role = roleRepository.findByName(RoleName.GUEST)
                .orElseThrow(() -> new IllegalStateException("Default role GUEST not found"));

        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(role);
        userRepository.save(user);

    // build principal and generate token via JwtService
    var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + (role != null ? role.getName() : "GUEST")));
    var principal = new CustomUserPrincipal(user.getId(), user.getEmail(), null, authorities, Boolean.TRUE.equals(user.getIsActive()));
    return jwtService.generateToken(principal);
    }

    public String login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

    var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + (user.getRole() != null ? user.getRole().getName() : "GUEST")));
    var principal = new CustomUserPrincipal(user.getId(), user.getEmail(), null, authorities, Boolean.TRUE.equals(user.getIsActive()));
    return jwtService.generateToken(principal);
    }
}
