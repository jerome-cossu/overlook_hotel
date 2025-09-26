package com.example.overlook_hotel.repository.hotel;

import com.example.overlook_hotel.model.entity.Room;
import com.example.overlook_hotel.model.enums.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByRoomNumber(String roomNumber);
    List<Room> findByRoomType(String roomType);
    List<Room> findByStatus(RoomStatus status);
}
