package com.example.overlook_hotel.dto.report;

import java.time.LocalDate;
import java.math.BigDecimal;

public class RevenueReportDto {
    private LocalDate date;
    private BigDecimal revenue;
    
    // getters/setters
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }
}
