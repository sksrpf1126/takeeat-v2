package com.back.takeeat.controller;

import com.back.takeeat.domain.user.Member;
import com.back.takeeat.dto.order.response.OrderResponse;
import com.back.takeeat.security.LoginMember;
import com.back.takeeat.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Value("${KAKAO_API_KEY}")
    String KAKAO_API_KEY;

    @GetMapping("/order")
    public String order(@LoginMember Member member, Model model) {
        Long memberId = member.getId();

        OrderResponse orderResponse = orderService.getOrderInfo(memberId);

        model.addAttribute("KAKAO_API_KEY", KAKAO_API_KEY);
        model.addAttribute("orderResponse", orderResponse);
        return "order/orderPage";
    }
}
