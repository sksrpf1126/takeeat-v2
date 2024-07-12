package com.back.takeeat.controller;

import com.back.takeeat.dto.marketMenu.response.MarketMenuResponse;
import com.back.takeeat.service.MarketMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class MarketMenuController {

    private final MarketMenuService marketMenuService;

    @Value("${KAKAO_API_KEY}")
    String KAKAO_API_KEY;

    @GetMapping("{marketId}/menu")
    public String menuList(@PathVariable("marketId") Long marketId, Model model) {
        MarketMenuResponse marketMenuResponse = marketMenuService.getMarketMenu(marketId);

        model.addAttribute("KAKAO_API_KEY", KAKAO_API_KEY);
        model.addAttribute("marketMenuResponse", marketMenuResponse);
        model.addAttribute("gpsService", true);
        return "order/orderMenu";
    }
}
