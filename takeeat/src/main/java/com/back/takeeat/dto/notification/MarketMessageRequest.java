package com.back.takeeat.dto.notification;

import com.back.takeeat.domain.order.OrderStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MarketMessageRequest {
    private Long marketId;
    private Long orderId;
    private OrderStatus currentOrderStatus;
    private OrderStatus selectOrderStatus;
}
