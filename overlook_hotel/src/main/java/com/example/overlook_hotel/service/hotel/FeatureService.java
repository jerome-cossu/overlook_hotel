package com.example.overlook_hotel.service;

import com.example.overlook_hotel.model.Feature;
import com.example.overlook_hotel.repository.FeatureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeatureService {
    private final FeatureRepository featureRepository;

    public List<Feature> getAllFeatures() {
        return featureRepository.findAll();
    }

    public Feature getFeatureById(long id) {
        return featureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feature not found with id " + id));
    }

    public Feature createFeature(Feature feature) {
        return featureRepository.save(feature);
    }

    public Feature updateFeature(long id, Feature featureDetails) {
        Feature feature = getFeatureById(id);

        feature.setName(featureDetails.getName());
        return featureRepository.save(feature);
    }

    public void deleteFeature(long id) {
        featureRepository.deleteById(id);
    }
}
