package com.example.overlook_hotel.service.auth;

import com.example.overlook_hotel.model.entity.User;
import com.example.overlook_hotel.repository.auth.UserRepository;
import com.example.overlook_hotel.config.CustomUserPrincipal;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + (user.getRole() != null ? user.getRole().getName() : "GUEST")));

        return new CustomUserPrincipal(user.getId(), user.getEmail(), user.getPasswordHash(), authorities, Boolean.TRUE.equals(user.getIsActive()));
    }
}
