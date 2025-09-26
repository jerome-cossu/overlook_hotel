package com.example.overlook_hotel.service.hotel;

import com.example.overlook_hotel.model.entity.User;
import com.example.overlook_hotel.repository.auth.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public User updateProfile(Long id, String firstName, String lastName, String phone) {
        User u = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setPhoneNumber(phone);
        return userRepository.save(u);
    }
}
