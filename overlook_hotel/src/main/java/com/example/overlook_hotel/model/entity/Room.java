package com.example.overlook_hotel.model.entity;

import com.example.overlook_hotel.model.enums.RoomStatus;

import jakarta.persistence.*;
import lombok.Setter;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "rooms")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Room {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_number", nullable = false, unique = true)
    private String roomNumber;

    @Column(name = "room_type") private String roomType;
    private Integer capacity = 1;

    @Column(name = "base_price", precision = 10, scale = 2)
    private BigDecimal basePrice = BigDecimal.ZERO;

    private String description;
    @Column(name = "floor_number") private Integer floorNumber;

    @Enumerated(EnumType.STRING)
    @Column(name="status", length=32, nullable=false)
    private RoomStatus status = RoomStatus.AVAILABLE;

    @Column(name = "is_accessible") private Boolean isAccessible = false;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RoomFeature> roomFeatures;

    @Column(name = "created_at") 
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at") 
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Version
    @Column(name = "version")
    private Integer version;
    
}
