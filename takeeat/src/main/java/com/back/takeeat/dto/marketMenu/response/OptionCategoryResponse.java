package com.back.takeeat.dto.marketMenu.response;

import com.back.takeeat.domain.option.OptionSelect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptionCategoryResponse {

    Long menuId;

    Long optionCategoryId;
    String optionCategoryName;
    int optionMaxCount;
    OptionSelect optionSelect;
}
