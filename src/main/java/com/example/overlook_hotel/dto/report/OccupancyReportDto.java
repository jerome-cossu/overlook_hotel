package com.example.overlook_hotel.dto.report;

import lombok.*;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OccupancyReportDto {
    
    private LocalDate date;
    private Integer totalRooms;
    private Integer occupiedRooms;
    private Double occupancyRate;
    
}