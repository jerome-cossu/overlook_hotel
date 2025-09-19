package com.example.overlook_hotel.repository;
import com.example.overlook_hotel.model.Booking;
import com.example.overlook_hotel.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);
    List<Booking> findByUser(User user);

    @Query("SELECT b FROM Booking b WHERE b.checkIn = :checkIn")
    List<Booking> findByCheckIn(LocalDate checkIn);
    @Query("SELECT b FROM Booking b WHERE b.checkIn = :checkOut")
    List<Booking> findByCheckOut(LocalDate checkOut);
}
