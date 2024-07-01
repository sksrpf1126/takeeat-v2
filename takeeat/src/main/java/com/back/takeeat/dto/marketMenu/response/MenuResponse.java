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
public class MenuResponse {

    Long menuCategoryId;
    String menuCategoryName;

    Long menuId;
    String menuName;
    String menuIntroduction;
    int menuMaxCount;
    String menuImage;
    int menuPrice;

    Long optionCategoryId;
    String optionCategoryName;
    int optionMaxCount;
    OptionSelect optionSelect;

    Long optionId;
    String optionName;
    int optionPrice;
}
