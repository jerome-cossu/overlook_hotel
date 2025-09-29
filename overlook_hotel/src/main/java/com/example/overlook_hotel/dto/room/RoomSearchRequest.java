package com.example.overlook_hotel.dto.room;

import lombok.*;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RoomSearchRequest {
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer guests;
    private String roomType;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private List<String> featureCodes;
    private Boolean accessibleOnly;
    
}