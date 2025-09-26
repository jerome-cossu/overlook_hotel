package com.example.overlook_hotel.dto.room;

import java.math.BigDecimal;
import java.util.List;

public class RoomDto {
    private Long id;
    private String roomNumber;
    private String roomType;
    private Integer capacity;
    private BigDecimal basePrice;
    private String description;
    private Integer floorNumber;
    private String status;
    private Boolean isAccessible;
    private List<String> features;

    // getters/setters

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public Integer getCapacity() {
        return capacity;
    }
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }
    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getFloorNumber() {
        return floorNumber;
    }
    public void setFloorNumber(Integer floorNumber) {
        this.floorNumber = floorNumber;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getIsAccessible() {
        return isAccessible;
    }
    public void setIsAccessible(Boolean isAccessible) {
        this.isAccessible = isAccessible;
    }

    public List<String> getFeatures() {
        return features;
    }
    public void setFeatures(List<String> features) {
        this.features = features;
    }
}
