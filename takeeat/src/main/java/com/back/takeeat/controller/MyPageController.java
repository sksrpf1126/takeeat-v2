package com.back.takeeat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/myPage")
public class MyPageController {

    @GetMapping("/orderList")
    public String orderList() {
        return "myPage/orderList";
    }

    @GetMapping("/orderDetail")
    public String orderDetail() {
        return "myPage/orderDetail";
    }

    @GetMapping("/writeReview")
    public String write() {
        return "myPage/reviewForm";
    }

    @GetMapping("/reviewList")
    public String reviewList() {
        return "myPage/reviewList";
    }
}
