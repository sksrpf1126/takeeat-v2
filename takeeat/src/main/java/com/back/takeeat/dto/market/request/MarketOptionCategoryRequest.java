package com.back.takeeat.dto.market.request;

import com.back.takeeat.domain.menu.Menu;
import com.back.takeeat.domain.option.Option;
import com.back.takeeat.domain.option.OptionCategory;
import com.back.takeeat.domain.option.OptionSelect;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MarketOptionCategoryRequest {

    private String optionCategoryName;
    private int optionMaxCount;
    private OptionSelect optionSelect;
    private List<MarketOptionRequest> options;
    private Menu menu;

    public OptionCategory toOptionCategory(Menu menu) {
        List<Option> optionList = options.stream()
                .map(MarketOptionRequest::toOption)
                .collect(Collectors.toList());

        return OptionCategory.builder()
                .menu(menu)
                .optionCategoryName(optionCategoryName)
                .optionMaxCount(optionMaxCount)
                .optionSelect(optionSelect)
                .build();
    }
}
