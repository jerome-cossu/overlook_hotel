package com.example.overlook_hotel.controller.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/guest")
public class GuestController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "guest/dashboard";
    }

    @GetMapping("/reservation")
    public String reservation() {
        return "guest/reservation";
    }
    
}
