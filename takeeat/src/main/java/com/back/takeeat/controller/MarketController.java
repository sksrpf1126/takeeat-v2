package com.back.takeeat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/market")
public class MarketController {

    @GetMapping("/info")
    public String marketInfo() {
        return "/market/marketInfo";
    }
    @GetMapping("/menu")
    public String marketMenu() {
        return "/market/marketMenu";
    }
    @GetMapping("/option")
    public String marketOption() {
        return "/market/marketOption";
    }
}
