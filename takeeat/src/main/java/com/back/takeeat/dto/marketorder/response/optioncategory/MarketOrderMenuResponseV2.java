package com.back.takeeat.dto.marketorder.response.optioncategory;

import com.back.takeeat.domain.option.OptionCategory;
import com.back.takeeat.domain.order.OrderMenu;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MarketOrderMenuResponseV2 {
    private String menuName;
    private int menuPrice;
    private int orderQuantity;
    List<OptionWithCategoryResponse> orderOptionResponses;

    public static MarketOrderMenuResponseV2 of(OrderMenu orderMenu, List<OptionCategory> optionCategories) {
        return MarketOrderMenuResponseV2.builder()
                .menuName(orderMenu.getMenu().getMenuName())
                .menuPrice(orderMenu.getMenu().getMenuPrice())
                .orderQuantity(orderMenu.getOrderQuantity())
                .orderOptionResponses(OptionWithCategoryResponse.listOf(optionCategories))
                .build();
    }

//    public static List<MarketOrderMenuResponseTwo> listOf(List<OrderMenu> orderMenus) {
//        return orderMenus.stream()
//                .map(MarketOrderMenuResponseTwo::of)
//                .collect(Collectors.toList());
//    }

}
