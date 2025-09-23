package com.example.overlook_hotel.service;

import com.example.overlook_hotel.model.Role;
import com.example.overlook_hotel.model.User;
import com.example.overlook_hotel.repository.RoleRepository;
import com.example.overlook_hotel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

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

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email " + email));
    }


        public User getMockUser() {
        return userRepository.findById(6L)
                .orElseThrow(() -> new RuntimeException("User with ID 6 not found"));
    }
}
