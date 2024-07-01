package com.back.takeeat.controller;

import com.back.takeeat.dto.market.request.MarketInfoRequest;
import com.back.takeeat.service.MarketInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/market")
public class MarketController {

    private final MarketInfoService marketInfoService;

    @GetMapping("/info")
    public String marketInfo(@ModelAttribute MarketInfoRequest marketInfoRequest, Model model) {

        model.addAttribute("marketInfoRequest", marketInfoRequest);

        return "/market/marketInfo";
    }


    @PostMapping("/save")
    public String registerMarket(/*@Valid */@ModelAttribute MarketInfoRequest marketInfoRequest, Model model) {
        /*if (result.hasErrors()) {
            return "/market/marketInfo";
        }*/
        marketInfoService.register(marketInfoRequest.marketInfoRequest());
        return "redirect:/market/menu";
    }
    @GetMapping("/menu")
    public String marketMenu() {
        return "/market/marketMenu";
    }
    @GetMapping("/option")
    public String marketOption() {
        return "/market/marketOption";
    }

    @GetMapping("/review")
    public String marketReview() {
        return "market/marketReview";
    }

}
