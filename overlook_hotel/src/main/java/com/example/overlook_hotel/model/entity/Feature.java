package com.example.overlook_hotel.model.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.Set;

@Entity
@Table(name = "features")
public class Feature {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private Boolean isActive = true;

    @OneToMany(mappedBy = "feature")
    private Set<RoomFeature> roomFeatures;

    @Column(name = "created_at") 
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at") 
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    // getters/setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<RoomFeature> getRoomFeatures() {
        return roomFeatures;
    }

    public void setRoomFeatures(Set<RoomFeature> roomFeatures) {
        this.roomFeatures = roomFeatures;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
