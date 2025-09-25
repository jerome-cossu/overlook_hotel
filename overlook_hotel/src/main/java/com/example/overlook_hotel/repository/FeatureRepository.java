package com.example.overlook_hotel.repository;

import com.example.overlook_hotel.model.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FeatureRepository extends JpaRepository<Feature, Long> {
    Optional<Feature> findByCode(String code);
    Optional<Feature> findByName(String name);
}
