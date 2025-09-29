package com.example.overlook_hotel.service.hotel;

import com.example.overlook_hotel.dto.report.OccupancyReportDto;
import com.example.overlook_hotel.dto.report.RevenueReportDto;
import com.example.overlook_hotel.model.entity.Reservation;
import com.example.overlook_hotel.model.entity.Room;
import com.example.overlook_hotel.model.enums.ReservationStatus;
import com.example.overlook_hotel.repository.hotel.ReservationRepository;
import com.example.overlook_hotel.repository.hotel.RoomRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReportService {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    public ReportService(RoomRepository roomRepository,
                         ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional(readOnly = true)
    public OccupancyReportDto getOccupancy(LocalDate date) {
        // total rooms
        int totalRooms = (int) roomRepository.count();

        // occupied rooms = reservations where date ∈ [checkInDate, checkOutDate) and status is active
    List<Reservation> active = reservationRepository.findActiveReservationsForDate(date, List.of(ReservationStatus.BOOKED, ReservationStatus.CHECKED_IN));
        int occupied = (int) active.stream()
                .map(Reservation::getRoom)
                .map(Room::getId)
                .distinct()
                .count();

        double rate = totalRooms == 0 ? 0.0 : (occupied / (double) totalRooms) * 100.0;

        return OccupancyReportDto.builder()
                .date(date)
                .totalRooms(totalRooms)
                .occupiedRooms(occupied)
                .occupancyRate(Math.round(rate * 100.0) / 100.0) // two-decimal percent
                .build();
    }

    @Transactional(readOnly = true)
    public RevenueReportDto getRevenue(LocalDate date) {
        // revenue for reservations that include the given date (prorate by nights if desired).
        // Simplest approach: sum totalPrice of reservations whose checkInDate == date (or whose stay includes date)
    List<Reservation> reservations = reservationRepository.findReservationsForDate(date, List.of(ReservationStatus.BOOKED, ReservationStatus.CHECKED_IN, ReservationStatus.CHECKED_OUT));

        // Sum totalPrice; null-safe
        BigDecimal sum = reservations.stream()
                .map(r -> r.getTotalPrice() == null ? BigDecimal.ZERO : r.getTotalPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        RevenueReportDto dto = new RevenueReportDto();
        dto.setDate(date);
        dto.setRevenue(sum);
        return dto;
    }
}