package com.back.takeeat.dto.myPage.response;

import com.back.takeeat.dto.cart.response.CartMenuResponse;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OrderMenuResponse {

    private Long orderMenuId;
    private int orderQuantity;
    private int orderPrice;

    private String menuName;
    private int menuPrice;

    public static OrderMenuResponse create(Long orderMenuId, int orderQuantity, int orderPrice, String menuName, int menuPrice) {
        return OrderMenuResponse.builder()
                .orderMenuId(orderMenuId)
                .orderQuantity(orderQuantity)
                .orderPrice(orderPrice)
                .menuName(menuName)
                .menuPrice(menuPrice)
                .build();
    }
}
