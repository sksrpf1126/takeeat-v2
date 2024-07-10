package com.back.takeeat.dto.market.request;

import com.back.takeeat.domain.option.Option;
import com.back.takeeat.domain.option.OptionCategory;
import com.back.takeeat.domain.option.OptionSelect;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MarketOptionCategoryRequest {

    private String optionCategoryName;

    private int optionMaxCount;

    private OptionSelect optionSelect;

    private List<MarketOptionRequest> options;

    public OptionCategory toOptionCategory() {
        List<Option> optionList = options.stream()
                .map(MarketOptionRequest::toOption)
                .collect(Collectors.toList());

        // 디버깅 포인트: 변환된 옵션 리스트 출력
        System.out.println("변환된 옵션 리스트: " + optionList);

        return OptionCategory.builder()
                .optionCategoryName(optionCategoryName)
                .optionMaxCount(optionMaxCount)
                .optionSelect(optionSelect)
                .build();
    }
}
