package com.example.overlook_hotel.service;

import com.example.overlook_hotel.model.Booking;
import com.example.overlook_hotel.model.Room;
import com.example.overlook_hotel.model.User;
import com.example.overlook_hotel.repository.BookingRepository;
import com.example.overlook_hotel.repository.RoomRepository;
import com.example.overlook_hotel.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    public List<Booking> getAllBookings(){
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id " + id));
    }
    
    public Booking createBooking(Booking booking) {
        booking.setId(null);
        return bookingRepository.save(booking);
    }

  public Booking updateBooking(Long id, Booking bookingDetails) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id " + id));

  
        booking.setCheckIn(bookingDetails.getCheckIn());
        booking.setCheckOut(bookingDetails.getCheckOut());

        if (bookingDetails.getUser() != null) {
            User user = userRepository.findById(bookingDetails.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("User not found with id " + bookingDetails.getUser().getId()));
            booking.setUser(user);
        }

        if (bookingDetails.getRooms() != null) {
            List<Room> updatedRooms = roomRepository.findAllById(
                    bookingDetails.getRooms()
                            .stream()
                            .map(Room::getId)
                            .toList()
            );

            for (Room room : updatedRooms) {
                room.setBooking(booking);
            }
            booking.setRooms(updatedRooms);
        }

        return bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
}
