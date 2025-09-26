package com.example.overlook_hotel.controller.api;

import com.example.overlook_hotel.model.entity.Feature;
import com.example.overlook_hotel.service.hotel.FeatureService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/features")
@RequiredArgsConstructor
public class FeatureController {

    @Autowired
    private FeatureService featureService;

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