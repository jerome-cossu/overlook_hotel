package com.example.overlook_hotel.service;

import com.example.overlook_hotel.model.Booking;
import com.example.overlook_hotel.model.User;
import com.example.overlook_hotel.model.Room;
import com.example.overlook_hotel.repository.BookingRepository;
import com.example.overlook_hotel.repository.UserRepository;
import com.example.overlook_hotel.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    // ✅ Injection par constructeur
    public BookingService(BookingRepository bookingRepository,
                          UserRepository userRepository,
                          RoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
    }

    public Booking createBooking(Booking booking) {
        // Récupérer l’utilisateur existant
        User user = userRepository.findById(booking.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        booking.setUser(user);

        // Récupérer les chambres existantes
        List<Room> rooms = booking.getRooms().stream()
                .map(r -> roomRepository.findById(r.getId())
                        .orElseThrow(() -> new RuntimeException("Room not found")))
                .collect(Collectors.toList());
        booking.setRooms(rooms);

        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
}
