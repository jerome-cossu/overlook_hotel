package com.example.overlook_hotel.repository;

import com.example.overlook_hotel.model.RoomFeature;
import com.example.overlook_hotel.model.Room;
import com.example.overlook_hotel.model.Feature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomFeatureRepository extends JpaRepository<RoomFeature, Long> {
    List<RoomFeature> findByRoom(Room room);
    List<RoomFeature> findByFeature(Feature feature);
    Optional<RoomFeature> findByRoomAndFeature(Room room, Feature feature);
}