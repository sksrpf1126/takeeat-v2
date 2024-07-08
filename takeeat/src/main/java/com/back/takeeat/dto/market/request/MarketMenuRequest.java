package com.back.takeeat.dto.market.request;

import com.back.takeeat.domain.menu.Menu;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
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
    @Positive(message = "메뉴 가격은 양수여야 합니다.")
    private int menuPrice;

    public Menu toMenu() {
        return Menu.builder()
                .menuName(menuName)
                .menuIntroduction(menuIntroduction)
                .menuImage(menuImage)
                .menuPrice(menuPrice)
                .build();
    }
}
