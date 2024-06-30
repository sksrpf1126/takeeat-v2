package com.back.takeeat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/notification")
public class NotificationController {

    @GetMapping("/member")
    public String notificationMember() {
        return "notification/notificationMember";
    }



}
