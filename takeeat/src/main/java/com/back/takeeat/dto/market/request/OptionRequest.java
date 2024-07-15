package com.back.takeeat.dto.market.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class OptionRequest {
    List<MarketOptionCategoryRequest> marketOptionCategoryRequestList;
}
