package com.back.takeeat.controller;

import com.back.takeeat.domain.market.Market;
import com.back.takeeat.dto.mainPage.response.MarketInfoResponse;
import com.back.takeeat.service.MarketListService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MarketListController {

    private final MarketListService marketListService;

    @Value("${KAKAO_API_KEY}")
    String KAKAO_API_KEY;

    @GetMapping("{marketCategory}/list")
    public String marketList(@PathVariable("marketCategory") String marketCategory
                            , Model model
                            , HttpSession session
                            , Market market) {
        List<MarketInfoResponse> marketInfoResponse = marketListService.getMarketInfo(market);
        Double latitude = (Double) session.getAttribute("latitude");
        Double longitude = (Double) session.getAttribute("longitude");
        model.addAttribute("listCount", marketInfoResponse.size());
        model.addAttribute("latitude", latitude);
        model.addAttribute("longitude", longitude);
        model.addAttribute("marketInfoResponse", marketInfoResponse);
        model.addAttribute("KAKAO_API_KEY", KAKAO_API_KEY);
        return "mainPage/marketList";
    }

}
