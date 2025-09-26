package com.example.overlook_hotel.repository.hotel;

import com.example.overlook_hotel.model.entity.Room;
import com.example.overlook_hotel.model.enums.ReservationStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByRoomNumber(String roomNumber);

    @Query("""
        SELECT r FROM Room r
        WHERE (:roomType IS NULL OR r.roomType = :roomType)
          AND (:minCapacity IS NULL OR r.capacity >= :minCapacity)
          AND NOT EXISTS (
            SELECT 1 FROM com.overlook.app.model.Reservation res
            WHERE res.room = r
              AND res.status IN :activeStatuses
              AND res.checkInDate < :checkOut
              AND res.checkOutDate > :checkIn
            )
    """)

    List<Room> findAvailableRooms(
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut,
            @Param("activeStatuses") List<ReservationStatus> activeStatuses,
            @Param("roomType") String roomType,
            @Param("minCapacity") Integer minCapacity
    );
}
