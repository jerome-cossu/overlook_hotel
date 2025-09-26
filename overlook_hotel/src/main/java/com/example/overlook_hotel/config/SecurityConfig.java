package com.example.overlook_hotel.config;

import com.example.overlook_hotel.config.JwtFilter;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Autowired
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // public/auth endpoints
                .requestMatchers("/api/auth/**", "/login", "/register", "/css/**", "/js/**", "/img/**", "/", "/actuator/health").permitAll()
                // manager endpoints
                .requestMatchers("/api/manager/**", "/manager/**").hasRole("MANAGER")
                // employee endpoints
                .requestMatchers("/api/employee/**", "/employee/**").hasAnyRole("EMPLOYEE","MANAGER")
                // guest endpoints
                .requestMatchers("/api/guest/**").hasAnyRole("GUEST","EMPLOYEE","MANAGER")
                // other API endpoints require authentication
                .requestMatchers("/api/**").authenticated()
                // UI pages
                .anyRequest().permitAll()
            )
            .exceptionHandling(Customizer.withDefaults()); // default handlers — customize AuthenticationEntryPoint or AccessDeniedHandler


        http.addFilterBefore((Filter) jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}