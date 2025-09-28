package com.example.overlook_hotel.controller.api;

import com.example.overlook_hotel.dto.reservation.AvailabilityRequest;
import com.example.overlook_hotel.dto.reservation.CreateReservationRequest;
import com.example.overlook_hotel.dto.reservation.ReservationDto;
import com.example.overlook_hotel.service.hotel.ReservationService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/availability")
    public ResponseEntity<List<?>> findAvailableRooms(@RequestBody AvailabilityRequest req) {
        var rooms = reservationService.findAvailableRooms(req);
        return ResponseEntity.ok(rooms); // map to Room DTO if needed
    }

    @PostMapping
    public ResponseEntity<ReservationDto> create(@RequestBody CreateReservationRequest dto) {
        var created = reservationService.createReservation(dto);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getById(id));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ReservationDto> cancel(@PathVariable Long id, @RequestParam Long userId) {
        // change this to get userId from JWT / security context
        var cancelled = reservationService.cancelReservation(id, userId);
        return ResponseEntity.ok(cancelled);
    }
}
