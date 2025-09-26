package com.example.overlook_hotel.util;

import com.example.overlook_hotel.dto.room.RoomDto;
import com.example.overlook_hotel.dto.reservation.ReservationDto;
import com.example.overlook_hotel.dto.user.UserProfileDto;
import com.example.overlook_hotel.model.entity.Room;
import com.example.overlook_hotel.model.entity.Reservation;
import com.example.overlook_hotel.model.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public final class Mapper {

    private Mapper() {}

    // Room -> RoomDto
    public static RoomDto toRoomDto(Room r) {
        if (r == null) return null;
        RoomDto d = new RoomDto();
        d.setId(r.getId());
        d.setRoomNumber(r.getRoomNumber());
        d.setRoomType(r.getRoomType());
        d.setCapacity(r.getCapacity());
        d.setBasePrice(r.getBasePrice());
        d.setDescription(r.getDescription());
        d.setFloorNumber(r.getFloorNumber());
        d.setStatus(r.getStatus() == null ? null : r.getStatus().name());
        d.setIsAccessible(r.getIsAccessible());
        // features: collect codes/names if present
        if (r.getRoomFeatures() != null) {
            d.setFeatures(r.getRoomFeatures().stream()
                .map(f -> f.getFeature() == null ? null : f.getFeature().getCode())
                .filter(code -> code != null)
                .collect(Collectors.toList()));
        }
        return d;
    }

    public static List<RoomDto> toRoomDtoList(List<Room> rooms) {
        if (rooms == null) return List.of();
        return rooms.stream().map(Mapper::toRoomDto).collect(Collectors.toList());
    }

    // Reservation -> ReservationDto
    public static ReservationDto toReservationDto(Reservation r) {
        if (r == null) return null;
        ReservationDto d = new ReservationDto();
        d.setId(r.getId());
        d.setUserId(r.getUser() == null ? null : r.getUser().getId());
        d.setRoomId(r.getRoom() == null ? null : r.getRoom().getId());
        d.setLeadGuestName(r.getLeadGuestName());
        d.setLeadGuestPhone(r.getLeadGuestPhone());
        d.setCheckInDate(r.getCheckInDate());
        d.setCheckOutDate(r.getCheckOutDate());
        d.setGuestsCount(r.getGuestsCount());
        d.setTotalPrice(r.getTotalPrice());
        d.setStatus(r.getStatus() == null ? null : r.getStatus().name());
        d.setSpecialRequests(r.getSpecialRequests());
        return d;
    }

    public static List<ReservationDto> toReservationDtoList(List<Reservation> list) {
        if (list == null) return List.of();
        return list.stream().map(Mapper::toReservationDto).collect(Collectors.toList());
    }

    // User -> UserProfileDto
    public static UserProfileDto toUserProfileDto(User u) {
        if (u == null) return null;
        UserProfileDto d = new UserProfileDto();
        d.setId(u.getId());
        d.setEmail(u.getEmail());
        d.setUsername(u.getUsername());
        d.setFirstName(u.getFirstName());
        d.setLastName(u.getLastName());
        d.setPhoneNumber(u.getPhoneNumber());
        d.setDateOfBirth(u.getDateOfBirth());
        d.setProfilePhotoUrl(u.getProfilePhotoUrl());
        return d;
    }
}
