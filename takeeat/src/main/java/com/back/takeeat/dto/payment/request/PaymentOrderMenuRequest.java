package com.back.takeeat.dto.payment.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentOrderMenuRequest {

    private Long menuId;
    private int orderQuantity;
    List<Long> optionIds;

}
