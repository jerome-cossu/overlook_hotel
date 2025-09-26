package com.example.overlook_hotel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerDashboardController {

    @GetMapping("/customerdashboard")
    public String customerdashboardPage() {
        return "customerdashboard";
    }
}