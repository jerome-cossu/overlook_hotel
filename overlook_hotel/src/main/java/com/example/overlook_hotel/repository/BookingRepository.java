package com.example.overlook_hotel.repository;
import com.example.overlook_hotel.model.Booking;
import com.example.overlook_hotel.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;



@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);
    List<Booking> findByUser(User user);

    List<Booking> findByCheckIn(LocalDate checkIn);
    List<Booking> findByCheckOut(LocalDate checkOut);

    List<Booking> findByCheckInBetween(LocalDate start, LocalDate end);
}
