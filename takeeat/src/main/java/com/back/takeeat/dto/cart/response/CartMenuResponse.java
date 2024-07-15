package com.back.takeeat.dto.cart.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CartMenuResponse {

    private Long cartMenuId;
    private int cartQuantity;
    private int cartPrice;

    private Long menuId;
    private String menuName;
    private int menuPrice;

    public static CartMenuResponse create(Long cartMenuId, int cartQuantity, int cartPrice, Long menuId,  String menuName, int menuPrice) {
        return CartMenuResponse.builder()
                .cartMenuId(cartMenuId)
                .cartQuantity(cartQuantity)
                .cartPrice(cartPrice)
                .menuId(menuId)
                .menuName(menuName)
                .menuPrice(menuPrice)
                .build();
    }
}
