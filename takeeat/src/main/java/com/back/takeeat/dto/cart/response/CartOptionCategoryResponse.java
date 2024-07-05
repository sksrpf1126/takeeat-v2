package com.back.takeeat.dto.cart.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartOptionCategoryResponse {

    private Long cartMenuId;

    private Long optionCategoryId;
    private String optionCategoryName;
}
