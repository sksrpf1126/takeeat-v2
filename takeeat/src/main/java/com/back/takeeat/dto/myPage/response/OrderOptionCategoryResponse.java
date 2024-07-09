package com.back.takeeat.dto.myPage.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderOptionCategoryResponse {

    private Long orderMenuId;

    private Long optionCategoryId;
    private String optionCategoryName;
}
