package com.back.takeeat.dto.cart.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartOptionResponse {

    Long optionCategoryId;

    String optionName;
}
