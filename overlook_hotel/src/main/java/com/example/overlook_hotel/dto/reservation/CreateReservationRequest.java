package com.example.overlook_hotel.dto.reservation;

import java.time.LocalDate;

public class CreateReservationRequest {
    private Long roomId;
    private Long userId; // optional if creating as guest
    private String leadGuestName;
    private String leadGuestPhone;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer guestsCount = 1;
    private String specialRequests;
    
    // getters/setters

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLeadGuestName() {
        return leadGuestName;
    }

    public void setLeadGuestName(String leadGuestName) {
        this.leadGuestName = leadGuestName;
    }

    public String getLeadGuestPhone() {
        return leadGuestPhone;
    }

    public void setLeadGuestPhone(String leadGuestPhone) {
        this.leadGuestPhone = leadGuestPhone;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public Integer getGuestsCount() {
        return guestsCount;
    }

    public void setGuestsCount(Integer guestsCount) {
        this.guestsCount = guestsCount;
    }

    public String getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }
}