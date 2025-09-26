package com.example.overlook_hotel.dto.room;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;

public class RoomSearchRequest {
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer guests;
    private String roomType;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private List<String> featureCodes;
    private Boolean accessibleOnly;
    
    // getters/setters
    
    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public Integer getGuests() {
        return guests;
    }

    public void setGuests(Integer guests) {
        this.guests = guests;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public List<String> getFeatureCodes() {
        return featureCodes;
    }

    public void setFeatureCodes(List<String> featureCodes) {
        this.featureCodes = featureCodes;
    }

    public Boolean getAccessibleOnly() {
        return accessibleOnly;
    }

    public void setAccessibleOnly(Boolean accessibleOnly) {
        this.accessibleOnly = accessibleOnly;
    }
}