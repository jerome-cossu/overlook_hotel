package com.example.overlook_hotel.dto.report;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.math.BigDecimal;

@Getter @Setter
public class RevenueReportDto {
    private LocalDate date;
    private BigDecimal revenue;
    
}
