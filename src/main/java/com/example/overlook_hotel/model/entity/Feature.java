package com.example.overlook_hotel.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "features")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Feature {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true) 
    private String code;

    @Column(nullable = false) 
    private String name;
    private String description;

    @Column(name = "icon_url") 
    private String iconUrl;

    private String category;

    @Column(name = "is_active") 
    @Builder.Default
    private Boolean isActive = true;

    @OneToMany(mappedBy = "feature")
    private Set<RoomFeature> roomFeatures;

    @Column(name = "created_at") 
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at") 
    @Builder.Default
    private Instant updatedAt = Instant.now();

}
