package com.example.overlook_hotel.model.entity;

import com.example.overlook_hotel.model.enums.RoomStatus;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.math.BigDecimal;

@Entity
@Table(name = "rooms", uniqueConstraints = {
    @UniqueConstraint(name = "uc_room_number", columnNames = "room_number")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_number", nullable = false, unique = true)
    private String roomNumber;

    @Column(name = "room_type")
    private String roomType;

    @Column(name = "capacity")
    @Builder.Default
    private Integer capacity = 1;

    @Column(name = "base_price", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal basePrice = BigDecimal.ZERO;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "floor_number")
    private Integer floorNumber;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name="status", length=32, nullable=false)
    private RoomStatus status = RoomStatus.AVAILABLE;

    @Column(name = "is_accessible")
    @Builder.Default
    private Boolean isAccessible = false;

    @Column(name = "created_at", updatable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    @Builder.Default
    private Instant updatedAt = Instant.now();

    @Version
    @Builder.Default
    @Column(name = "version")
    private Integer version = 1;
    
}
