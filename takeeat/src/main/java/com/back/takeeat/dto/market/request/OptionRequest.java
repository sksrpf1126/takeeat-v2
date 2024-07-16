package com.back.takeeat.dto.market.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptionRequest {
    List<MarketOptionCategoryRequest> categories;
}
