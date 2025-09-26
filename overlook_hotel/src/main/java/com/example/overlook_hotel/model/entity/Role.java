package com.example.overlook_hotel.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

import com.example.overlook_hotel.model.enums.RoleName;

@Entity
@Table(name = "roles", indexes = {
    @Index(columnList = "name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleName name;

    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();
}