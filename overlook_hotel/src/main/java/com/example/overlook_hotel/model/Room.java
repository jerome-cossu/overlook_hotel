package com.example.overlook_hotel.model;
import java.util.HashSet;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

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

    @Column(name = "room_picture")
    private String picture;

    @ManyToMany
    @JoinTable(
        name = "room_feature",
        joinColumns = @JoinColumn(name = "room_id"),
        inverseJoinColumns = @JoinColumn(name = "feature_id")
    )
    private Set<Feature> features = new HashSet<>();

    @Column(nullable = false)
    private int price;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    @JsonBackReference
    private Booking booking;
}