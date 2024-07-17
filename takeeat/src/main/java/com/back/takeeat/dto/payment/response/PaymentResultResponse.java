package com.back.takeeat.dto.payment.response;

import com.back.takeeat.domain.market.Market;
import com.back.takeeat.domain.order.Order;
import com.back.takeeat.domain.payment.Payment;
import com.back.takeeat.domain.payment.PaymentMethod;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentResultResponse {

    private String marketName;
    private String orderCode;
    private PaymentMethod paymentMethod;
    private Long totalPrice;

    public static PaymentResultResponse of(Market market, Order order, Payment payment) {
        return PaymentResultResponse.builder()
                .marketName(market.getMarketName())
                .orderCode(order.getOrderCode())
                .paymentMethod(payment.getMethod())
                .totalPrice(payment.getAmount())
                .build();
    }
}
