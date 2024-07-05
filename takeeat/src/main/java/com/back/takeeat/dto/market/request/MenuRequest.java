package com.back.takeeat.dto.market.request;

import lombok.Getter;

import java.util.List;

@Getter
public class MenuRequest {
    private List<MarketMenuCategoryRequest> categories;
}
