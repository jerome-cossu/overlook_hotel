package com.example.overlook_hotel.service.hotel;

import com.example.overlook_hotel.model.entity.Role;
import com.example.overlook_hotel.model.entity.User;
import com.example.overlook_hotel.repository.auth.RoleRepository;
import com.example.overlook_hotel.repository.auth.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    public User createUser(User user) {
        Role role = roleRepository.findById(user.getRole().getId())
                .orElseThrow(() -> new RuntimeException("Role not found with id " + user.getRole().getId()));
        user.setRole(role);

        return userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);

        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setEmail(userDetails.getEmail());
        user.setPassword(userDetails.getPassword());
        user.setLoyaltyPoints(userDetails.getLoyaltyPoints());

        Role role = roleRepository.findById(userDetails.getRole().getId())
                .orElseThrow(() -> new RuntimeException("Role not found with id " + userDetails.getRole().getId()));
        user.setRole(role);

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    public User registerUser(User user) {
        // Assign default role (e.g., "USER")
        Role userRole = roleRepository.findByName("USER")
            .orElseThrow(() -> new RuntimeException("Role USER not found"));
        user.setRole(userRole);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
