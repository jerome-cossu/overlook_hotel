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

    // find overlapping reservations for a given room
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

    // list reservations for user in period
    List<Reservation> findByUserIdAndCheckInDateBetween(Long userId, LocalDate start, LocalDate end);

    // reservations that include the given date and have one of the provided statuses
    @Query("""
      SELECT r FROM Reservation r
      WHERE :date >= r.checkInDate AND :date < r.checkOutDate
        AND r.status IN :statuses
    """)
    List<Reservation> findReservationsForDate(@Param("date") LocalDate date,
                                             @Param("statuses") List<ReservationStatus> statuses);

    // convenience for 'active' reservations (booked, checked_in)
    @Query("""
      SELECT r FROM Reservation r
      WHERE :date >= r.checkInDate AND :date < r.checkOutDate
        AND r.status IN :activeStatuses
    """)
    List<Reservation> findActiveReservationsForDate(@Param("date") LocalDate date,
                                                   @Param("activeStatuses") List<ReservationStatus> activeStatuses);

}