package com.back.takeeat.controller;


import com.back.takeeat.dto.market.request.MarketInfoRequest;
import com.back.takeeat.domain.market.MarketInfoForm;
import com.back.takeeat.service.MarketInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/market")
public class MarketController {

    private final MarketInfoService marketInfoService;

    @GetMapping("/info")
    public String marketInfo(@ModelAttribute("marketInfo") MarketInfoRequest marketInfoRequest, Model model) {

        model.addAttribute("marketInfoRequest", marketInfoRequest);

        return "/market/marketInfo";
    }


    @PostMapping("/save")
    public String registerMarket(@Valid @ModelAttribute("marketInfo") MarketInfoRequest marketInfoRequest, BindingResult result) {
        if (result.hasErrors()) {
            return "/market/marketInfo";
        }
        marketInfoService.register(marketInfoRequest.marketInfoRequest());
        return "redirect:/market/menu";
    }


    // 가게 이름 중복검사
    @GetMapping("/marketName/check")
    public ResponseEntity<Boolean> checkMarketNameDuplicate(@RequestParam(value="marketName") String marketName) {
        boolean isAvailable = marketInfoService.checkMarketNameDuplicate(marketName);
        return ResponseEntity.ok(isAvailable);
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
