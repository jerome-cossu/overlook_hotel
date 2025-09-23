package com.example.overlook_hotel.service;

import com.example.overlook_hotel.model.Booking;
import com.example.overlook_hotel.model.Room;
import com.example.overlook_hotel.model.User;
import com.example.overlook_hotel.repository.RoomRepository;
import com.example.overlook_hotel.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking reserveRoom(Long roomId, User user, LocalDate checkIn, LocalDate checkOut) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (room.isOccupancy()) {
            throw new RuntimeException("Room already booked");
        }

        // Mettre la chambre en occupée
        LocalDate today = LocalDate.now();
        if ((today.isAfter(checkIn) || today.isEqual(checkIn)) 
        && (today.isBefore(checkOut) || today.isEqual(checkOut))) {
            room.setOccupancy(true);
        }
        roomRepository.save(room);

        // Créer la réservation
        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setUser(user);
        booking.setCheckIn(checkIn);
        booking.setCheckOut(checkOut);

        return bookingRepository.save(booking);
    }
}
