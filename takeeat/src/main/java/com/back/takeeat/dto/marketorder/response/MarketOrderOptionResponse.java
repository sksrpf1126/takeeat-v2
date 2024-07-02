package com.back.takeeat.dto.marketorder.response;

import com.back.takeeat.domain.order.OrderOption;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class MarketOrderOptionResponse {
    private String optionName;
    private int optionPrice;

    public static MarketOrderOptionResponse of(OrderOption orderOption) {
        return MarketOrderOptionResponse.builder()
                .optionName(orderOption.getOption().getOptionName())
                .optionPrice(orderOption.getOption().getOptionPrice())
                .build();
    }

    public static List<MarketOrderOptionResponse> listOf(List<OrderOption> orderOptions) {
        return orderOptions.stream()
                .map(MarketOrderOptionResponse::of)
                .collect(Collectors.toList());
    }
}
