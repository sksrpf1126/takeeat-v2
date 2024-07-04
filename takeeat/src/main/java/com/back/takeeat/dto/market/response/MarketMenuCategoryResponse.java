package com.back.takeeat.dto.market.response;

import com.back.takeeat.domain.menu.MenuCategory;
import com.back.takeeat.dto.market.request.MarketMenuCategoryRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MarketMenuCategoryResponse {

    @NotBlank(message = "메뉴 카테고리명은 필수입니다.")
    private String menuCategoryName;

    public static MarketMenuCategoryResponse of(MenuCategory menuCategory) {
        return MarketMenuCategoryResponse.builder()
                .menuCategoryName(menuCategory.getMenuCategoryName())
                .build();
    }
}
