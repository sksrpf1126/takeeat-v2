package com.back.takeeat.dto.marketorder.response.optioncategory;

import com.back.takeeat.domain.option.Option;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class MarketOrderOptionResponseV2 {
    private String optionName;
    private int optionPrice;

    public static MarketOrderOptionResponseV2 of(Option option) {
        return MarketOrderOptionResponseV2.builder()
                .optionName(option.getOptionName())
                .optionPrice(option.getOptionPrice())
                .build();
    }

    public static List<MarketOrderOptionResponseV2> listOf(List<Option> options) {
        return options.stream()
                .map(MarketOrderOptionResponseV2::of)
                .collect(Collectors.toList());
    }
}
