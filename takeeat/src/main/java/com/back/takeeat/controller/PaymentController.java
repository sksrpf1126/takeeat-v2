package com.back.takeeat.controller;

import com.back.takeeat.domain.user.Member;
import com.back.takeeat.dto.payment.request.PaymentOrderRequest;
import com.back.takeeat.security.LoginMember;
import com.back.takeeat.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/result")
    public String before() {
        return "/payment/orderPaymentResult";
    }

    //화면에서 결제 완료 시 처리하는 로직
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/complete")
    @ResponseBody
    public Long paymentComplete(@LoginMember Member member, @RequestBody PaymentOrderRequest paymentOrderRequest) {

        return paymentService.registerPayment(member, paymentOrderRequest);
    }

}
