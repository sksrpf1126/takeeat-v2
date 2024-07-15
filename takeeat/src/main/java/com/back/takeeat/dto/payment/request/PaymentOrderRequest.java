package com.back.takeeat.dto.payment.request;

import com.back.takeeat.domain.payment.Payment;
import com.back.takeeat.domain.payment.PaymentMethod;
import com.back.takeeat.domain.user.Member;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentOrderRequest {

    private Long marketId;
    private int amount;
    private String orderCode;
    private String requirement;
    private List<PaymentOrderMenuRequest> orderMenuRequests;

    public Payment toPayment(Member member) {
        return Payment.builder()
                .method(PaymentMethod.CARD)
                .amount((long) this.amount)
                .member(member)
                .build();
    }

}
