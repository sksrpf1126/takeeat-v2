package com.back.takeeat.dto.cart;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class CartMenuIdAndOptionCategoryId {

    private Long cartMenuId;
    private Long optoinCategoryId;
}
