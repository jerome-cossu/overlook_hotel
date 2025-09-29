package com.example.overlook_hotel.controller.api;

import com.example.overlook_hotel.service.hotel.UserService;
import com.example.overlook_hotel.config.CustomUserPrincipal;
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
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Unauthenticated");
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserPrincipal) {
            return ((CustomUserPrincipal) principal).getId();
        }
        // fallback: if principal is a username string, resolve via service (not implemented here)
        throw new SecurityException("Unable to extract user id from authentication principal");
    }

    private boolean hasRole(Authentication authentication, String roleName) {
        if (authentication == null) return false;
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + roleName));
    }
}
