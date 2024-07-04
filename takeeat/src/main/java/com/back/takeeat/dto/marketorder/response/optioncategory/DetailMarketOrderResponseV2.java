package com.back.takeeat.dto.marketorder.response.optioncategory;

import com.back.takeeat.domain.order.Order;
import com.back.takeeat.domain.order.OrderMenu;
import com.back.takeeat.domain.order.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class DetailMarketOrderResponseV2 {

    private Long memberId;
    private String phone;
    private LocalDateTime orderCreateTime;
    private OrderStatus orderStatus;
    private int menuCount;
    private int totalPrice;
    private String requirement;
    private List<MarketOrderMenuResponseV2> orderMenuResponses;

    public static DetailMarketOrderResponseV2 of(Order order, List<MarketOrderMenuResponseV2> orderMenuResponses) {
        Integer menuCount = order.getOrderMenus().stream()
                .map(OrderMenu::getOrderQuantity)
                .reduce((x, y) -> x + y).orElse(0);

        return DetailMarketOrderResponseV2.builder()
                .memberId(order.getMember().getId())
                .phone(order.getMember().getPhone())
                .orderCreateTime(order.getCreatedTime())
                .orderStatus(order.getOrderStatus())
                .menuCount(menuCount)
                .totalPrice(order.getTotalPrice())
                .requirement(order.getRequirement())
                .orderMenuResponses(orderMenuResponses)
                .build();
    }

}
