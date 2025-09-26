package com.example.overlook_hotel.repository.hotel;

import com.example.overlook_hotel.model.entity.Feature;
import com.example.overlook_hotel.model.entity.Room;
import com.example.overlook_hotel.model.entity.RoomFeature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomFeatureRepository extends JpaRepository<RoomFeature, Long> {
    List<RoomFeature> findByRoom(Room room);
    List<RoomFeature> findByFeature(Feature feature);
    Optional<RoomFeature> findByRoomAndFeature(Room room, Feature feature);
}