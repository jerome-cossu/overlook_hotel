package com.example.overlook_hotel.controller.api;

import com.example.overlook_hotel.dto.report.OccupancyReportDto;
import com.example.overlook_hotel.dto.report.RevenueReportDto;
import com.example.overlook_hotel.service.hotel.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/occupancy")
    public ResponseEntity<OccupancyReportDto> occupancy(@RequestParam(required = false) String date) {
        LocalDate d = date == null ? LocalDate.now() : LocalDate.parse(date);
        return ResponseEntity.ok(reportService.getOccupancy(d));
    }

    @GetMapping("/revenue")
    public ResponseEntity<RevenueReportDto> revenue(@RequestParam(required = false) String date) {
        LocalDate d = date == null ? LocalDate.now() : LocalDate.parse(date);
        return ResponseEntity.ok(reportService.getRevenue(d));
    }
}
