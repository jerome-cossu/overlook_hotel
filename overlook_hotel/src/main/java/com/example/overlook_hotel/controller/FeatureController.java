package com.example.overlook_hotel.controller;
import com.example.overlook_hotel.model.Feature;
import com.example.overlook_hotel.service.FeatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/features")
@RequiredArgsConstructor
public class FeatureController {

    private final FeatureService featureService;

    @GetMapping
    public List<Feature> getAllFeatures() {
        return featureService.getAllFeatures();
    }

    @GetMapping("/{id}")
    public Feature getFeatureById(@PathVariable int id) {
        return featureService.getFeatureById(id);
    }

    @PostMapping
    public Feature createFeature(@RequestBody Feature feature) {
        return featureService.createFeature(feature);
    }

    @PutMapping("/{id}")
    public Feature updateFeature(@PathVariable int id, @RequestBody Feature feature) {
        return featureService.updateFeature(id, feature);
    }

    @DeleteMapping("/{id}")
    public void deleteFeature(@PathVariable int id) {
        featureService.deleteFeature(id);
    }
}