package com.example.overlook_hotel.controller;

import com.example.overlook_hotel.model.User;
import com.example.overlook_hotel.model.Booking;
import com.example.overlook_hotel.service.BookingService;
import com.example.overlook_hotel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;

    @ResponseBody
    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @PostMapping("/reserve/{roomId}")
    public String reserveRoom(
            @PathVariable Long roomId,
            @RequestParam("checkIn") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam("checkOut") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut) {

        return "redirect:/rooms/" + roomId;
    }
}
