package com.back.takeeat.dto.marketorder.response;

import com.back.takeeat.domain.order.OrderMenu;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class MarketOrderMenuResponse {
    private String menuName;
    private int menuPrice;
    private int orderQuantity;
    List<MarketOrderOptionResponse> orderOptionResponses;

    public static MarketOrderMenuResponse of(OrderMenu orderMenu) {
        return MarketOrderMenuResponse.builder()
                .menuName(orderMenu.getMenu().getMenuName())
                .menuPrice(orderMenu.getMenu().getMenuPrice())
                .orderQuantity(orderMenu.getOrderQuantity())
                .orderOptionResponses(MarketOrderOptionResponse.listOf(orderMenu.getOrderOptions()))
                .build();
    }

    public static List<MarketOrderMenuResponse> listOf(List<OrderMenu> orderMenus) {
        return orderMenus.stream()
                .map(MarketOrderMenuResponse::of)
                .collect(Collectors.toList());
    }

}
