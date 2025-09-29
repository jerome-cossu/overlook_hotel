package com.example.overlook_hotel.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PasswordResetRequest {
    private String email;
    private String newPassword;
    private String confirmPassword;
    
}
