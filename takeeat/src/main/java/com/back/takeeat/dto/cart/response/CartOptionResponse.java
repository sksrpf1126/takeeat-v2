package com.back.takeeat.dto.cart.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartOptionResponse {

    Long cartMenuId;

    Long optionCategoryId;
    String optionCategoryName;

    Long optionId;
    String optionName;

}
