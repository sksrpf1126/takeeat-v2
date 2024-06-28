package com.back.takeeat.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Value("${KAKAO_API_KEY}")
    String KAKAO_API_KEY;

    @GetMapping("/menuList")
    public String menuList(Model model) {
        model.addAttribute("KAKAO_API_KEY", KAKAO_API_KEY);
        return "order/orderMenu";
    }

    @GetMapping("/cart")
    public String cart() {
        return "order/cart";
    }
}
