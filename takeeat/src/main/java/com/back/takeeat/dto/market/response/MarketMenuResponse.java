package com.back.takeeat.dto.market.response;

import com.back.takeeat.domain.menu.Menu;
import com.back.takeeat.domain.menu.MenuCategory;
import com.back.takeeat.dto.market.request.MarketMenuRequest;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MarketMenuResponse {
    private Long menuCategoryId;

    private String menuName;

    public MarketMenuRequest marketMenuRequest() {
        return MarketMenuRequest.builder()
                .menuName(menuName)
                .build();
    }

    public static Menu of(MenuCategory menuCategory, Menu menu) {
        return Menu.builder()
                .id(menuCategory.getId())
                .menuName(menu.getMenuName())
                .build();
    }
}
