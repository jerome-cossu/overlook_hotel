package com.example.overlook_hotel.model.entity;

import com.example.overlook_hotel.model.enums.ReservationStatus;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "reservations",
       indexes = @Index(name = "idx_reservations_room_dates", columnList = "room_id,check_in_date,check_out_date"))
public class Reservation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "lead_guest_name") 
    private String leadGuestName;

    @Column(name = "lead_guest_phone") 
    private String leadGuestPhone;

    @Column(name = "check_in_date", nullable = false) 
    private LocalDate checkInDate;

    @Column(name = "check_out_date", nullable = false) 
    private LocalDate checkOutDate;

    @Column(name = "guests_count") 
    private Integer guestsCount = 1;

    @Column(name = "total_price", precision = 10, scale = 2) 
        private BigDecimal totalPrice = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name="status", length=32, nullable=false)
    private ReservationStatus status = ReservationStatus.CHECKED_OUT;

    @Column(name = "special_requests") 
    private String specialRequests;
    
    @Column(name = "cancellation_policy") 
    private String cancellationPolicy;

    @Column(name = "created_at") 
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at") 
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    @Version
    @Column(name = "version")
    private Integer version = 1;

    // getters/setters
}
