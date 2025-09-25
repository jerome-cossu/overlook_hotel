package com.example.overlook_hotel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Email is missing")
    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "Password is missing")
    @Size(min = 6, message = "The password should be at least 6 char")
    private String password;

    @NotBlank(message = "Last name is missins")
    @JsonProperty("first_name")
    private String firstName;

    @NotBlank(message = "First name is missing")
    @JsonProperty("last_name")
    private String lastName;

    private Long roleId;
}

