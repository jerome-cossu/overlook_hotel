package com.example.overlook_hotel.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

import com.example.overlook_hotel.model.enums.RoomStatus;

import java.math.BigDecimal;

@Entity
@Table(name = "rooms", indexes = {
    @Index(columnList = "room_number"),
    @Index(columnList = "room_type")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_number", nullable = false, unique = true)
    private String roomNumber;

    @Column(name = "room_type")
    private String roomType;

    @Builder.Default
    @Column(nullable = false)
    private Integer capacity = 1;

    @Builder.Default
    @Column(name = "base_price", precision = 10, scale = 2)
    private BigDecimal basePrice = BigDecimal.ZERO;

    private String description;

    @Column(name = "floor_number")
    private Integer floorNumber;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomStatus status = RoomStatus.AVAILABLE;

    @Builder.Default
    @Column(name = "is_accessible", nullable = false)
    private Boolean isAccessible = false;

    @Builder.Default
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Builder.Default
    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    @Version
    private Integer version;
}
