package com.example.overlook_hotel.controller.api;

import com.example.overlook_hotel.dto.room.RoomDto;
import com.example.overlook_hotel.dto.room.RoomSearchRequest;
import com.example.overlook_hotel.service.hotel.RoomService;
import com.example.overlook_hotel.model.entity.Room;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // Example: GET /api/rooms  (RoomSearchRequest can be bound from query params if adding @ModelAttribute)
    @GetMapping
    public ResponseEntity<List<RoomDto>> search(@ModelAttribute RoomSearchRequest req) {
        List<Room> rooms = roomService.findAll(); // simple implementation
        List<RoomDto> dtos = rooms.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDto> get(@PathVariable Long id) {
        return roomService.findById(id)
                .map(r -> ResponseEntity.ok(toDto(r)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private RoomDto toDto(Room r) {
        RoomDto d = new RoomDto();
        d.setId(r.getId());
        d.setRoomNumber(r.getRoomNumber());
        d.setRoomType(r.getRoomType());
        d.setCapacity(r.getCapacity());
        d.setBasePrice(r.getBasePrice());
        d.setDescription(r.getDescription());
        d.setFloorNumber(r.getFloorNumber());
        d.setStatus(r.getStatus() == null ? null : r.getStatus().name());
        d.setIsAccessible(r.getIsAccessible());
        return d;
    }
}
