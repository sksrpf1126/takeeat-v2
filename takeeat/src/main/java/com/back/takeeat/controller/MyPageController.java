package com.back.takeeat.controller;

import com.back.takeeat.dto.myPage.response.OrderListResponse;
import com.back.takeeat.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/myPage")
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/orderList")
    public String orderList(Model model) {
        Long memberId = 1L; //(임시)로그인 회원

        List<OrderListResponse> orderListResponse = myPageService.getOrderList(memberId);

        model.addAttribute("orderListResponse", orderListResponse);
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
