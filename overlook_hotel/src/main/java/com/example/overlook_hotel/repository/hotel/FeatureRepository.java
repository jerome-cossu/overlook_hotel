package com.example.overlook_hotel.repository.hotel;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.overlook_hotel.model.entity.Feature;

import java.util.Optional;

public interface FeatureRepository extends JpaRepository<Feature, Long> {
    Optional<Feature> findByCode(String code);
    Optional<Feature> findByName(String name);
}
