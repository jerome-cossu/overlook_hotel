package com.example.overlook_hotel.service.hotel;

import com.example.overlook_hotel.repository.hotel.RoomRepository;
import com.example.overlook_hotel.model.entity.Room;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    public Optional<Room> findById(Long id) {
        return roomRepository.findById(id);
    }

    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    public List<Room> findByType(String type) {
        return roomRepository.findByRoomType(type);
    }
}
