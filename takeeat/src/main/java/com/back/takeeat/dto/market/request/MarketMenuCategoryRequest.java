package com.back.takeeat.dto.market.request;

import com.back.takeeat.domain.market.Market;
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

    private Market market;

    public MenuCategory toMenuCategory(Market market) {
        List<Menu> menuList = menus.stream()
                .map(MarketMenuRequest::toMenu) // MarketMenuRequest 를 Menu 로 변환
                .collect(Collectors.toList());

        // 디버깅 포인트: 변환된 메뉴 리스트 출력
        System.out.println("변환된 메뉴 리스트: " + menuList);

        return MenuCategory.builder()
                .market(market)
                .menuCategoryName(menuCategoryName)
                .build();
    }

}
