package com.example.overlook_hotel.repository.hotel;

import com.example.overlook_hotel.model.entity.Reservation;
import com.example.overlook_hotel.model.enums.ReservationStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserId(Long userId);

    @Query("""
      SELECT r FROM Reservation r
      WHERE r.room.id = :roomId
        AND r.status IN :activeStatuses
        AND r.checkInDate < :checkOut
        AND r.checkOutDate > :checkIn
    """)
    List<Reservation> findOverlappingReservations(
            @Param("roomId") Long roomId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut,
            @Param("activeStatuses") List<ReservationStatus> activeStatuses
    );

    @Query("""
      SELECT r FROM Reservation r
      WHERE r.checkInDate >= :from AND r.checkOutDate <= :to
    """)
    List<Reservation> findByDateRange(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );
}