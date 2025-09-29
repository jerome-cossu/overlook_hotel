package com.example.overlook_hotel.dto.reservation;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CancelReservationRequest {
    
    private Long reservationId;
    private String reason;

}