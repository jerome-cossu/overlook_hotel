package com.example.overlook_hotel.repository;
import com.example.overlook_hotel.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    Room findByRoomNumber(int roomNumber);
    Room findByGuest(int Guest);
    Room findByOccupancy(boolean occupancy);
    Room findByPrice(int price);
}
