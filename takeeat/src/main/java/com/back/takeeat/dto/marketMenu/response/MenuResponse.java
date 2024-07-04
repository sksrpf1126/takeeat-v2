package com.back.takeeat.dto.marketMenu.response;

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
    String menuImage;
    int menuPrice;
}
