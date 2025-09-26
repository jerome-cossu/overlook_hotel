package com.example.overlook_hotel.model.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "users", indexes = @Index(columnList = "email"))
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "first_name") 
    private String firstName;

    @Column(name = "last_name") 
    private String lastName;

    @Column(name = "phone_number") 
    private String phoneNumber;

    @Column(name = "date_of_birth") 
    private LocalDate dateOfBirth;

    @Column(name = "profile_photo_url") 
    private String profilePhotoUrl;

    @Column(name = "is_active") 
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "created_at") 
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at") 
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    @Column(name = "last_login") 
    private OffsetDateTime lastLogin;

    @Version
    @Column(name = "version")
    private Integer version;

    // getters/setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
