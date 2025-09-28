package com.example.overlook_hotel.dto.reservation;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AvailabilityRequest {
    public LocalDate checkInDate;
    public LocalDate checkOutDate;
    public Integer capacity;
    public String roomType;
    
}
