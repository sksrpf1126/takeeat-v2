package com.back.takeeat.dto.myPage.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderOptionResponse {

    private Long orderMenuId;

    private Long optionCategoryId;

    private String optionName;
    private int optionPrice;
}
