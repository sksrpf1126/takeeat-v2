package com.back.takeeat.controller;

import com.back.takeeat.dto.mainPage.response.MarketInfoResponse;
import com.back.takeeat.service.MarketListService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MarketListController {

    private final MarketListService marketListService;

    @Value("${KAKAO_API_KEY}")
    String KAKAO_API_KEY;

    @GetMapping("{marketCategory}/list")
    public String marketList(@PathVariable("marketCategory") String marketCategory
                            , @RequestParam(name = "search", required = false, defaultValue = "") String search
                            , Model model
                            , HttpSession session) {
        Double latitude = (Double) session.getAttribute("latitude");
        Double longitude = (Double) session.getAttribute("longitude");
        List<MarketInfoResponse> marketInfoResponse = marketListService.getMarketInfo(marketCategory, latitude, longitude, search);

        model.addAttribute("listCount", marketInfoResponse.size());
        model.addAttribute("latitude", latitude);
        model.addAttribute("longitude", longitude);
        model.addAttribute("marketInfoResponse", marketInfoResponse);
        model.addAttribute("KAKAO_API_KEY", KAKAO_API_KEY);
        return "mainPage/marketList";
    }

}
