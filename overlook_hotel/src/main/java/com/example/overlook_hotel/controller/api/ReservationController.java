package com.example.overlook_hotel.controller.api;

import com.example.overlook_hotel.dto.reservation.CreateReservationRequest;
import com.example.overlook_hotel.dto.reservation.ReservationDto;
import com.example.overlook_hotel.dto.reservation.CancelReservationRequest;
import com.example.overlook_hotel.model.entity.Reservation;
import com.example.overlook_hotel.service.hotel.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationDto> create(@RequestBody CreateReservationRequest req) {
        Reservation r = reservationService.createReservation(
            req.getRoomId(),
            req.getUserId(),
            req.getLeadGuestName(),
            req.getLeadGuestPhone(),
            req.getCheckInDate(),
            req.getCheckOutDate(),
            req.getGuestsCount()
        );
        return ResponseEntity.ok(toDto(r));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReservationDto>> byUser(@PathVariable Long userId) {
        List<Reservation> list = reservationService.findByUserId(userId);
        return ResponseEntity.ok(list.stream().map(this::toDto).collect(Collectors.toList()));
    }

    @PostMapping("/cancel")
    public ResponseEntity<ReservationDto> cancel(@RequestBody CancelReservationRequest req) {
        Reservation r = reservationService.cancelReservation(req.getReservationId(), req.getReason());
        return ResponseEntity.ok(toDto(r));
    }

    private ReservationDto toDto(Reservation r) {
        ReservationDto d = new ReservationDto();
        d.setId(r.getId());
        d.setUserId(r.getUser() == null ? null : r.getUser().getId());
        d.setRoomId(r.getRoom().getId());
        d.setLeadGuestName(r.getLeadGuestName());
        d.setLeadGuestPhone(r.getLeadGuestPhone());
        d.setCheckInDate(r.getCheckInDate());
        d.setCheckOutDate(r.getCheckOutDate());
        d.setGuestsCount(r.getGuestsCount());
        d.setTotalPrice(r.getTotalPrice());
        d.setStatus(r.getStatus() == null ? null : r.getStatus().name());
        d.setSpecialRequests(r.getSpecialRequests());
        return d;
    }
}
