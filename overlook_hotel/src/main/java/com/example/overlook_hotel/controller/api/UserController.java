package com.example.overlook_hotel.controller.api;

import com.example.overlook_hotel.service.hotel.UserService;
import com.example.overlook_hotel.dto.user.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // public registration
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody CreateUserDto dto) {
        var created = userService.createUser(dto);
        return ResponseEntity.status(201).body(created);
    }

    // get own profile; uses Authentication principal (JWT) to get user id
    @GetMapping("/me")
    public ResponseEntity<UserDto> me(Authentication authentication) {
        Long userId = extractUserId(authentication);
        var dto = userService.getById(userId);
        return ResponseEntity.ok(dto);
    }


    // update profile (owner or manager)
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Long id,
                                          @Valid @RequestBody UpdateUserDto dto,
                                          Authentication authentication) {
        Long requesterId = extractUserId(authentication);
        boolean isManager = hasRole(authentication, "MANAGER");
        var updated = userService.updateProfile(id, dto, requesterId, isManager);
        return ResponseEntity.ok(updated);
    }

    // deactivate (manager only)
    @PostMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable Long id, Authentication authentication) {
        if (!hasRole(authentication, "MANAGER")) {
            return ResponseEntity.status(403).build();
        }
        userService.deactivateUser(id);
        return ResponseEntity.noContent().build();
    }

    // Helper to extract the user id from the Authentication principal.
    // Tries several common approaches:
    //  - parse authentication.getName() as a numeric id
    //  - reflectively call getId() on the principal object if present
    private Long extractUserId(Authentication authentication) {
        if (authentication == null) {
            throw new IllegalArgumentException("Authentication is required");
        }

        Object principal = authentication.getPrincipal();
        if (principal == null) {
            throw new IllegalArgumentException("Authentication principal is missing");
        }

        // Try parsing the name (some setups place the id in authentication.getName())
        String name = authentication.getName();
        if (name != null) {
            try {
                return Long.valueOf(name);
            } catch (NumberFormatException ignored) {
                // fall through to other strategies
            }
        }

        // Try to reflectively call getId() on the principal if available
        try {
            java.lang.reflect.Method m = principal.getClass().getMethod("getId");
            Object id = m.invoke(principal);
            if (id instanceof Number) {
                return ((Number) id).longValue();
            }
            if (id instanceof String) {
                return Long.valueOf((String) id);
            }
        } catch (NoSuchMethodException ignored) {
            // no getId method; fall through
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to extract id from principal", ex);
        }

        // As a last resort, try parsing the principal.toString()
        try {
            return Long.valueOf(principal.toString());
        } catch (Exception ex) {
            throw new IllegalStateException("Cannot extract user id from authentication principal");
        }
    }

    // Helper to check if the authentication has a given role.
    // Accepts both "ROLE_X" and "X" authority formats.
    private boolean hasRole(Authentication authentication, String role) {
        if (authentication == null || authentication.getAuthorities() == null) return false;
        String roleWithPrefix = role.startsWith("ROLE_") ? role : "ROLE_" + role;
        return authentication.getAuthorities().stream()
            .anyMatch(a -> roleWithPrefix.equals(a.getAuthority()) || role.equals(a.getAuthority()));
    }

}
