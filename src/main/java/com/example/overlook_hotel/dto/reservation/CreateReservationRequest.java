package com.example.overlook_hotel.dto.reservation;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter
public class CreateReservationRequest {
    
    public Long userId;
    public Long roomId;
    public String leadGuestName;
    public String leadGuestPhone;
    public LocalDate checkInDate;
    public LocalDate checkOutDate;
    public Integer guestsCount;
    public BigDecimal totalPrice;
    public String specialRequests;
}