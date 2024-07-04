package com.back.takeeat.dto.market.request;

import com.back.takeeat.domain.menu.MenuCategory;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class MarketMenuCategoryRequest {

    @NotBlank(message = "메뉴 카테고리명은 필수입니다.")
    private String menuCategoryName;

    private List<MarketMenuCategoryRequest> menuCategories;

    public MarketMenuCategoryRequest marketMenuCategoryRequest() {
        return MarketMenuCategoryRequest.builder()
                .menuCategoryName(menuCategoryName)
                .build();
    }

    public MenuCategory toMenuCategory() {
        return MenuCategory.builder()
                .menuCategoryName(menuCategoryName)
                .build();
    }
}
