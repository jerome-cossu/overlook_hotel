package com.example.overlook_hotel.dto.reservation;

import com.example.overlook_hotel.model.enums.ReservationStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReservationDto {
    public Long id;
    public Long userId;
    public Long roomId;
    public String leadGuestName;
    public String leadGuestPhone;
    public LocalDate checkInDate;
    public LocalDate checkOutDate;
    public Integer guestsCount;
    public BigDecimal totalPrice;
    public ReservationStatus status;
    public String specialRequests;
    public Integer version;
}