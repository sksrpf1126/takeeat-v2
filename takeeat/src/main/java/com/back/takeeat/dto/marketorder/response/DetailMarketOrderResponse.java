package com.back.takeeat.dto.marketorder.response;

import com.back.takeeat.domain.order.Order;
import com.back.takeeat.domain.order.OrderMenu;
import com.back.takeeat.domain.order.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class DetailMarketOrderResponse {

    private Long memberId;
    private String phone;
    private LocalDateTime orderCreateTime;
    private OrderStatus orderStatus;
    private int menuCount;
    private int totalPrice;
    private String requirement;
    private List<MarketOrderMenuResponse> orderMenuResponses;

    public static DetailMarketOrderResponse of(Order order) {
        Integer menuCount = order.getOrderMenus().stream()
                .map(OrderMenu::getOrderQuantity)
                .reduce((x, y) -> x + y).orElse(0);

        return DetailMarketOrderResponse.builder()
                .memberId(order.getMember().getId())
                .phone(order.getMember().getPhone())
                .orderCreateTime(order.getCreatedTime())
                .orderStatus(order.getOrderStatus())
                .menuCount(menuCount)
                .totalPrice(order.getTotalPrice())
                .requirement(order.getRequirement())
                .orderMenuResponses(MarketOrderMenuResponse.listOf(order.getOrderMenus()))
                .build();
    }

}
