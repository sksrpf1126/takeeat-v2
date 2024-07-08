package com.back.takeeat.controller;

import com.back.takeeat.common.exception.AccessDeniedException;
import com.back.takeeat.dto.myPage.response.OrderDetailResponse;
import com.back.takeeat.dto.myPage.response.OrderListResponse;
import com.back.takeeat.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/my")
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/order/list")
    public String orderList(Model model) {
        Long memberId = 1L; //(임시)로그인 회원

        List<OrderListResponse> orderListResponse = myPageService.getOrderList(memberId);

        model.addAttribute("orderListResponse", orderListResponse);
        return "myPage/orderList";
    }

    @GetMapping("/{orderId}/order")
    public String orderDetail(@PathVariable("orderId") Long orderId, Model model) {

        OrderDetailResponse orderDetailResponse = myPageService.getOrderDetail(orderId);

        model.addAttribute("orderDetailResponse", orderDetailResponse);
        return "myPage/orderDetail";
    }

    @GetMapping("/review/new")
    public String write(@RequestParam("orderId") Long orderId, Model model) {
        Long memberId = 1L; //(임시)로그인 회원

        String marketName = null;
        try {
            marketName = myPageService.getOrderMarketName(memberId, orderId);
        } catch (AccessDeniedException e) {
            return "errorPage/noAuthorityPage";
        }

        model.addAttribute("marketName", marketName);
        return "myPage/reviewForm";
    }

    @GetMapping("/review/list")
    public String reviewList() {
        return "myPage/reviewList";
    }
}
