package com.example.overlook_hotel.service.hotel;

import com.example.overlook_hotel.model.entity.Reservation;
import com.example.overlook_hotel.model.entity.Room;
import com.example.overlook_hotel.model.entity.User;
import com.example.overlook_hotel.model.enums.ReservationStatus;
import com.example.overlook_hotel.repository.hotel.ReservationRepository;
import com.example.overlook_hotel.repository.hotel.RoomRepository;
import com.example.overlook_hotel.repository.auth.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    private final List<ReservationStatus> ACTIVE_STATUSES = List.of(ReservationStatus.BOOKED, ReservationStatus.CHECKED_IN);

    @Transactional
    public Reservation createReservation(Long roomId, Long userId, String leadName, String leadPhone,
                                         LocalDate checkIn, LocalDate checkOut, Integer guestsCount) {

        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        // overlap check
        var conflicts = reservationRepository.findOverlappingReservations(roomId, checkIn, checkOut, ACTIVE_STATUSES);
        if (!conflicts.isEmpty()) {
            throw new IllegalStateException("Room not available for selected dates");
        }

        User user = null;
        if (userId != null) {
            user = userRepository.findById(userId).orElse(null);
        }

        Reservation r = new Reservation();
        r.setRoom(room);
        r.setUser(user);
        r.setLeadGuestName(leadName);
        r.setLeadGuestPhone(leadPhone);
        r.setCheckInDate(checkIn);
        r.setCheckOutDate(checkOut);
        r.setGuestsCount(guestsCount == null ? 1 : guestsCount);
        r.setStatus(ReservationStatus.BOOKED);
        // compute price simple: nights * basePrice
        long nights = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
        java.math.BigDecimal price = room.getBasePrice().multiply(java.math.BigDecimal.valueOf(Math.max(1, nights)));
        r.setTotalPrice(price);

        return reservationRepository.save(r);
    }

    @Transactional
    public Reservation cancelReservation(Long reservationId, String reason) {
        Reservation r = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        r.setStatus(ReservationStatus.CANCELLED);
        r.setSpecialRequests((r.getSpecialRequests() == null ? "" : r.getSpecialRequests() + "\n") +
                "Cancelled: " + reason);
        return reservationRepository.save(r);
    }
}
