package com.example.overlook_hotel.repository.hotel;

import com.example.overlook_hotel.model.entity.Room;
import com.example.overlook_hotel.model.enums.RoomStatus;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    
    // find rooms that do NOT have overlapping reservations in given range (using left join)
    @Query("""
      SELECT r FROM Room r
      WHERE (:capacity IS NULL OR r.capacity >= :capacity)
        AND (:roomType IS NULL OR r.roomType = :roomType)
        AND NOT EXISTS (
          SELECT 1 FROM Reservation res
          WHERE res.room = r
            AND res.status IN :activeStatuses
            AND NOT (res.check_out_date <= :start OR res.check_in_date >= :end)
        )
      """)
    List<Room> findAvailable(
      @Param("start") LocalDate start,
      @Param("end") LocalDate end,
      @Param("capacity") Integer capacity,
      @Param("roomType") String roomType,
      @Param("activeStatuses") List<RoomStatus> activeStatuses
    );

    Optional<Room> findByRoomNumber(String roomNumber);

    List<Room> findByRoomType(String roomType);

    List<Room> findByStatus(RoomStatus status);
}
