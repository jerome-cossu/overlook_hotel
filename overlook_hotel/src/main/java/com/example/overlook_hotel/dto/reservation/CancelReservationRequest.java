package com.example.overlook_hotel.dto.reservation;

public class CancelReservationRequest {
    private Long reservationId;
    private String reason;
    
    // getters/setters

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}