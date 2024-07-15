package com.back.takeeat.dto.payment.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentRequest {

    private Long marketId;
    private int amount;
    private String orderCode;
    private String requirement;

}
