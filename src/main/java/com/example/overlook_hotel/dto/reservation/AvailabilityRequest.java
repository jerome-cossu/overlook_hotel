package com.example.overlook_hotel.dto.reservation;

import java.time.LocalDate;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AvailabilityRequest {
    
    public LocalDate checkInDate;
    public LocalDate checkOutDate;
    public Integer capacity;
    public String roomType;
    
}
