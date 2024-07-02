package com.back.takeeat.dto.marketMenu.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuCategoryResponse {

    private String menuCategoryName;

    public static MenuCategoryResponse create(String menuCategoryName) {
        return MenuCategoryResponse.builder()
                .menuCategoryName(menuCategoryName)
                .build();
    }
}
