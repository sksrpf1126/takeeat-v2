package com.back.takeeat.dto.market.response;

import com.back.takeeat.domain.menu.Menu;
import com.back.takeeat.domain.menu.MenuCategory;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MenuCategoryNameResponse {
    private Long menuCategoryId;

    private Long menuId;

    private String menuName;

    public MenuCategoryNameResponse create() {
        return MenuCategoryNameResponse.builder()
                .menuCategoryId(menuCategoryId)
                .menuId(menuId)
                .menuName(menuName)
                .build();
    }

    public static Menu of(MenuCategory menuCategory, Menu menu) {
        return Menu.builder()
                .id(menuCategory.getId())
                .id(menu.getId())
                .menuName(menu.getMenuName())
                .build();
    }
}
