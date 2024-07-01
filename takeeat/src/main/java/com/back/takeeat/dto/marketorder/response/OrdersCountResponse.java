package com.back.takeeat.dto.marketorder.response;

import com.back.takeeat.domain.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrdersCountResponse {

    private OrderStatus orderStatus;
    private Long orderCount;

}
