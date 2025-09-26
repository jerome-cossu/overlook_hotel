package com.example.overlook_hotel.dto.report;

import java.time.LocalDate;

public class OccupancyReportDto {
    private LocalDate date;
    private Integer totalRooms;
    private Integer occupiedRooms;
    private Double occupancyRate;
    
    // getters/setters
    
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public Integer getTotalRooms() {
        return totalRooms;
    }
    public void setTotalRooms(Integer totalRooms) {
        this.totalRooms = totalRooms;
    }
    public Integer getOccupiedRooms() {
        return occupiedRooms;
    }
    public void setOccupiedRooms(Integer occupiedRooms) {
        this.occupiedRooms = occupiedRooms;
    } 
    public Double getOccupancyRate() {
        return occupancyRate;
    }
    public void setOccupancyRate(Double occupancyRate) {
        this.occupancyRate = occupancyRate;
    }
}