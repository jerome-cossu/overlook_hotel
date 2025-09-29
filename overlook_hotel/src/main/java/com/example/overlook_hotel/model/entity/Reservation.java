package com.example.overlook_hotel.model.entity;

import com.example.overlook_hotel.model.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "reservations")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // simple FK mapping; adjust fetch type as needed
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
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
    private ReservationStatus status = ReservationStatus.BOOKED;

    @Column(name = "special_requests", columnDefinition = "text")
    private String specialRequests;

    @Column(name = "cancellation_policy")
    private String cancellationPolicy;

    @Version
    @Column(name = "version")
    private Integer version;
}