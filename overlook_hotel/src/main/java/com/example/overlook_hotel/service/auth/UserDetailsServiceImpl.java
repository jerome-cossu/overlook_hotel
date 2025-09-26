package com.example.overlook_hotel.service.auth;

import com.example.overlook_hotel.model.entity.User;
import com.example.overlook_hotel.repository.auth.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String emailOrUsername) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(emailOrUsername)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + emailOrUsername));

        var authorities = Collections.singletonList(
            new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + user.getRole().getName())
        );

        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPasswordHash(),
            user.getIsActive(),
            true, true, true,
            authorities
        );
    }
}
