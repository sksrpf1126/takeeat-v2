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
    private String optionName;
    private int optionPrice;

    public static MarketOrderMenuResponse of(OrderMenu orderMenu) {
        return MarketOrderMenuResponse.builder()
                .menuName(orderMenu.getMenu().getMenuName())
                .menuPrice(orderMenu.getMenu().getMenuPrice())
                .orderQuantity(orderMenu.getOrderQuantity())
                .optionName(orderMenu.getOption().getOptionName())
                .optionPrice(orderMenu.getOption().getOptionPrice())
                .build();
    }

    public static List<MarketOrderMenuResponse> listOf(List<OrderMenu> orderMenus) {
        return orderMenus.stream()
                .map(MarketOrderMenuResponse::of)
                .collect(Collectors.toList());
    }

}
