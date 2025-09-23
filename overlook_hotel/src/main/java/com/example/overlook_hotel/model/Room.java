package com.example.overlook_hotel.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private int id;

    @Column(nullable = false, unique = true, name = "room_number")
    private int roomNumber;

    @Column(nullable = false)
    private int guest;

    @Column(nullable = false)
    private boolean occupancy;

    @Column(nullable = false)
    private int price;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Reservation booking;
}