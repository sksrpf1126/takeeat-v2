package com.back.takeeat.dto.marketorder.response;

import com.back.takeeat.domain.order.Order;
import com.back.takeeat.domain.order.OrderMenu;
import com.back.takeeat.domain.order.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class MarketOrdersResponse {

    private Long orderId;
    private int menuCount;
    private int totalPrice;
    private OrderStatus orderStatus;
    private boolean orderCheck;
    private LocalDateTime orderCreateTime;

    public static MarketOrdersResponse of(Order order) {

        Integer menuCount = order.getOrderMenus().stream()
                .map(OrderMenu::getOrderQuantity)
                .reduce((x, y) -> x + y).orElse(0);

        return MarketOrdersResponse.builder()
                .orderId(order.getId())
                .menuCount(menuCount)
                .totalPrice(order.getTotalPrice())
                .orderStatus(order.getOrderStatus())
                .orderCheck(order.isChecking())
                .orderCreateTime(order.getCreatedTime())
                .build();
    }

    public static List<MarketOrdersResponse> listOf(List<Order> orders) {
        return orders.stream()
                .map(MarketOrdersResponse::of)
                .collect(Collectors.toList());
    }

}
