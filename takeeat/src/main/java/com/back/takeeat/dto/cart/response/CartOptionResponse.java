package com.back.takeeat.dto.cart.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartOptionResponse {

    private Long cartMenuId;

    private Long optionCategoryId;

    private Long optionId;
    private String optionName;
    private int optionPrice;
}
