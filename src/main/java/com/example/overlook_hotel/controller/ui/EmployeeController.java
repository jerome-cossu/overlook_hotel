package com.example.overlook_hotel.controller.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EmployeeController {

    @GetMapping("/employee-dashboard")
    public String employeeHome() {
        return "employee-dashboard";
    }
    
}
