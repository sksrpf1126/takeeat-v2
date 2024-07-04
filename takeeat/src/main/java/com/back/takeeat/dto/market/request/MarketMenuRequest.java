package com.back.takeeat.dto.market.request;

import com.back.takeeat.domain.menu.Menu;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MarketMenuRequest {

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

    public Menu toMenu() {
        return Menu.builder()
                .menuName(menuName)
                .menuIntroduction(menuIntroduction)
                .menuImage(menuImage)
                .menuPrice(menuPrice)
                .build();
    }
}
