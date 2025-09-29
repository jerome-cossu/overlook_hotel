package com.example.overlook_hotel.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AuthResponse {
    private String token;
    private String tokenType = "Bearer";
    private Long userId;
    private String email;

    public AuthResponse(String token, Long userId, String email) {
        this.token = token;
        this.userId = userId;
        this.email = email;
    }

    public AuthResponse(String token, String tokenType, Long userId, String email) {
        this.token = token;
        this.tokenType = tokenType;
        this.userId = userId;
        this.email = email;
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "tokenType='" + tokenType + '\'' +
                ", userId=" + userId +
                ", email='" + email + '\'' +
                '}';
    }
}
