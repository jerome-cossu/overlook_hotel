package com.example.overlook_hotel.repository.hotel;

import com.example.overlook_hotel.model.entity.Reservation;
import com.example.overlook_hotel.model.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    // Find reservations for a room that overlap the given date range and are active (booked/checked_in)
    @Query("""
        select r from Reservation r
        where r.room.id = :roomId
          and r.status in :activeStatuses
          and r.checkInDate < :checkOut
          and r.checkOutDate > :checkIn
        """)
    List<Reservation> findOverlappingReservations(
        @Param("roomId") Long roomId,
        @Param("checkIn") LocalDate checkIn,
        @Param("checkOut") LocalDate checkOut,
        @Param("activeStatuses") List<ReservationStatus> activeStatuses
    );

    List<Reservation> findByDateRange(
        @Param("from") LocalDate from,
        @Param("to") LocalDate to
    );

    List<Reservation> findByUserId(Long userId);

    List<Reservation> findByStatus(ReservationStatus status);
}