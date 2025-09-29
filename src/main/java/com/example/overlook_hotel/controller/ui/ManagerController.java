package com.example.overlook_hotel.controller.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    @GetMapping("/admin-dashboard")
    public String dashboard() {
        return "manager/dashboard";
    }
    @GetMapping("/reservations")
    public String reservations() {
        return "reservations";
    }

    @GetMapping("/reservation")
    public String reservation() {
        return "reservation";
    }

    @GetMapping("/room-status")
    public String roomStatus() {
        return "room-status";
    }   

    @GetMapping("/rooms-crud")
    public String roomsCrud() {
        return "rooms-crud";
    }

    @GetMapping("/reports")
    public String reports() {
        return "reports";
    }
    
}
