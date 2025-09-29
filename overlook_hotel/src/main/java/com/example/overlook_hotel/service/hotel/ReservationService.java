package com.example.overlook_hotel.service.hotel;

import com.example.overlook_hotel.dto.reservation.ReservationDto;
import com.example.overlook_hotel.model.entity.Reservation;
import com.example.overlook_hotel.model.entity.Room;
import com.example.overlook_hotel.model.enums.RoomStatus;
import com.example.overlook_hotel.model.enums.ReservationStatus;
import com.example.overlook_hotel.repository.hotel.ReservationRepository;
import com.example.overlook_hotel.repository.hotel.RoomRepository;
import com.example.overlook_hotel.repository.auth.UserRepository;
import com.example.overlook_hotel.dto.reservation.AvailabilityRequest;
import com.example.overlook_hotel.dto.reservation.CreateReservationRequest;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    // statuses considered blocking availability
    private static final List<ReservationStatus> RESERVATION_ACTIVE_STATUSES = List.of(ReservationStatus.BOOKED, ReservationStatus.CHECKED_IN);
    private static final List<RoomStatus> ROOM_ACTIVE_STATUSES = List.of(RoomStatus.AVAILABLE);

    public ReservationService(ReservationRepository reservationRepository,
                              RoomRepository roomRepository,
                              UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }
    public List<Room> findAvailableRooms(AvailabilityRequest req) {
        return roomRepository.findAvailable(
            req.getCheckInDate(),
            req.getCheckOutDate(),
            req.getCapacity(),
            req.getRoomType(),
            ROOM_ACTIVE_STATUSES
        );
    }

    @Transactional
    public ReservationDto createReservation(CreateReservationRequest dto) {
        // load user and room (throw if not found)
        var user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        var room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        // business validations
        if (!dto.getCheckOutDate().isAfter(dto.getCheckInDate())) {
            throw new IllegalArgumentException("checkOutDate must be after checkInDate");
        }

        // check overlaps for chosen room
        var overlaps = reservationRepository.findOverlappingReservations(
                room.getId(),
                dto.getCheckInDate(),
                dto.getCheckOutDate(),
                RESERVATION_ACTIVE_STATUSES
        );
        if (!overlaps.isEmpty()) {
            throw new IllegalStateException("Room is not available for the selected dates");
        }

        // create and save
        var reservation = Reservation.builder()
                .user(user)
                .room(room)
                .leadGuestName(dto.getLeadGuestName())
                .leadGuestPhone(dto.getLeadGuestPhone())
                .checkInDate(dto.getCheckInDate())
                .checkOutDate(dto.getCheckOutDate())
                .guestsCount(Optional.ofNullable(dto.getGuestsCount()).orElse(1))
                .totalPrice(dto.getTotalPrice())
                .specialRequests(dto.getSpecialRequests())
                .status(ReservationStatus.BOOKED)
                .build();

        var saved = reservationRepository.save(reservation);
        return mapToDto(saved);
    }

    public ReservationDto getById(Long id) {
        var r = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        return mapToDto(r);
    }

    @Transactional
    public ReservationDto cancelReservation(Long id, Long requestingUserId) {
        var res = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        // minimal permission check example: guest can cancel own reservation, manager can cancel all
        if (!res.getUser().getId().equals(requestingUserId) /* && !isManager(...) */) {
            throw new SecurityException("Not allowed to cancel");
        }
        res.setStatus(ReservationStatus.CANCELLED);
        var saved = reservationRepository.save(res);
        return mapToDto(saved);
    }

    private ReservationDto mapToDto(Reservation r) {
        return ReservationDto.builder()
                .id(r.getId())
                .userId(r.getUser() != null ? r.getUser().getId() : null)
                .roomId(r.getRoom() != null ? r.getRoom().getId() : null)
                .leadGuestName(r.getLeadGuestName())
                .leadGuestPhone(r.getLeadGuestPhone())
                .checkInDate(r.getCheckInDate())
                .checkOutDate(r.getCheckOutDate())
                .guestsCount(r.getGuestsCount())
                .totalPrice(r.getTotalPrice())
                .status(r.getStatus())
                .specialRequests(r.getSpecialRequests())
                .version(r.getVersion())
                .build();
    }
}