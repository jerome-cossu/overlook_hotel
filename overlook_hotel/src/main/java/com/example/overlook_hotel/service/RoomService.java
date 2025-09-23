package com.example.overlook_hotel.service;

import com.example.overlook_hotel.model.Room;
import com.example.overlook_hotel.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;

    public List<Room> getAllRooms(){
        return roomRepository.findAll();
    }

    public Room getRoomById(int id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with id " + id));
    }
    
    public Room createRoom(Room room) {
    return roomRepository.save(room);
    }

    public Room updateRoom(int id, Room roomDetails) {
        Room room = getRoomById(id);

        room.setRoomNumber(roomDetails.getRoomNumber());
        room.setGuest(roomDetails.getGuest());
        room.setOccupancy(roomDetails.isOccupancy());
        room.setPrice(roomDetails.getPrice());
        room.setBooking(roomDetails.getBooking());

        return roomRepository.save(room);
    }

    @Transactional
    public Room updateOccupancy(int id, boolean occupancy) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with id " + id));

        room.setOccupancy(occupancy);
        return roomRepository.save(room);
    }

    public void deleteRoom(int id) {
        roomRepository.deleteById(id);
    }
}
