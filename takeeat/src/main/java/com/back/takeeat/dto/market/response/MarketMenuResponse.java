package com.back.takeeat.dto.market.response;

import com.back.takeeat.domain.menu.Menu;
import com.back.takeeat.dto.market.request.MarketMenuRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MarketMenuResponse {
    @NotBlank(message = "메뉴명은 필수입니다.")
    private String menuName;

    private String menuIntroduction;

    private String menuImage;

    @NotBlank(message = "가격은 필수입니다.")
    private int menuPrice;

    public MarketMenuRequest marketMenuRequest() {
        return MarketMenuRequest.builder()
                .menuName(menuName)
                .menuIntroduction(menuIntroduction)
                .menuImage(menuImage)
                .menuPrice(menuPrice)
                .build();
    }

    public static Menu toMenu(Menu menu) {
        return Menu.builder()
                .menuName(menu.getMenuName())
                .menuIntroduction(menu.getMenuIntroduction())
                .menuImage(menu.getMenuImage())
                .menuPrice(menu.getMenuPrice())
                .build();
    }
}
