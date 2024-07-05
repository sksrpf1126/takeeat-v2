package com.back.takeeat.dto.market.request;

import com.back.takeeat.domain.menu.Menu;
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

    private List<MarketMenuRequest> menus;

    public MenuCategory toMenuCategory() {
        List<Menu> menuList = menus.stream()
                .map(MarketMenuRequest::toMenu) // MarketMenuRequest 를 Menu 로 변환
                .collect(Collectors.toList());
        return MenuCategory.builder()
                .menuCategoryName(menuCategoryName)
                .menus(menuList) // Menu 객체 리스트로 설정
                .build();
    }

}
