package com.back.takeeat.dto.marketorder.response.optioncategory;

import com.back.takeeat.domain.option.OptionCategory;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class OptionWithCategoryResponse {

    private Long optionCategoryId;
    private String optionCategoryName;
    private List<MarketOrderOptionResponseV2> optionResponses;

    public static OptionWithCategoryResponse of(OptionCategory optionCategory) {
        return OptionWithCategoryResponse.builder()
                .optionCategoryId(optionCategory.getId())
                .optionCategoryName(optionCategory.getOptionCategoryName())
                .optionResponses(MarketOrderOptionResponseV2.listOf(optionCategory.getOptions()))
                .build();
    }

    public static List<OptionWithCategoryResponse> listOf(List<OptionCategory> optionCategories) {
        return optionCategories.stream()
                .map(OptionWithCategoryResponse::of)
                .collect(Collectors.toList());
    }

}
