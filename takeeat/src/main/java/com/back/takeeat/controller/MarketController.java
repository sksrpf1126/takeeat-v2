package com.back.takeeat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/market")
public class MarketController {

    @GetMapping("/review")
    public String marketInfo() {
        return "market/marketReview";
    }

}
