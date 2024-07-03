package com.back.takeeat.dto.cart.response;

import lombok.Builder;

@Builder
public class CartMenuResponse {

    Long cartMenuId;
    int cartQuantity;
    int cartPrice;

    String menuName;

    public static CartMenuResponse create(Long cartMenuId, int cartQuantity, int cartPrice, String menuName) {
        return CartMenuResponse.builder()
                .cartMenuId(cartMenuId)
                .cartQuantity(cartQuantity)
                .cartPrice(cartPrice)
                .menuName(menuName)
                .build();
    }
}
