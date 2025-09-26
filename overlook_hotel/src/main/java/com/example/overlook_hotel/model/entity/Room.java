package com.example.overlook_hotel.model.entity;

import com.example.overlook_hotel.model.enums.RoomStatus;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;

@Entity
@Table(name = "rooms")
public class Room {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at") 
    private OffsetDateTime updatedAt = OffsetDateTime.now();
    
    @Version
    @Column(name = "version")
    private Integer version = 1;

    // getters/setters
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}
