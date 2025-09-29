package com.example.overlook_hotel.dto.room;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
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

}
