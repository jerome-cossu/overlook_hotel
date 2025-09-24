package com.example.overlook_hotel.controller;

import com.example.overlook_hotel.model.Room;
import com.example.overlook_hotel.model.User;
import com.example.overlook_hotel.service.RoomService;
import com.example.overlook_hotel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@Controller
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomPageController {

    private final RoomService roomService;
    private final UserService userService;

    @GetMapping("/{id}")
    public String getRoomPage(@PathVariable Long id, Model model, Principal principal) {
        Room room = roomService.getRoomById(id);
        model.addAttribute("room", room);

        if (principal != null) {
            User user = userService.findByEmail(principal.getName());
            model.addAttribute("user", user);
        }

        return "room-details";
    }
}

