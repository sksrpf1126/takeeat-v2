package com.back.takeeat.controller;


import com.back.takeeat.domain.menu.MenuCategory;
import com.back.takeeat.dto.market.request.MarketInfoRequest;
import com.back.takeeat.dto.market.request.MarketMenuCategoryRequest;
import com.back.takeeat.dto.market.request.MarketMenuRequest;
import com.back.takeeat.service.MarketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/market")
public class MarketController {

    private final MarketService marketService;

    @GetMapping("/info")
    public String marketInfo(@ModelAttribute("marketInfo") MarketInfoRequest marketInfoRequest, Model model) {

        model.addAttribute("marketInfoRequest", marketInfoRequest);

        return "/market/marketInfo";
    }


    @PostMapping("/info/save")
    public String saveMarketInfo(@Valid @ModelAttribute("marketInfo") MarketInfoRequest marketInfoRequest, BindingResult result) {
        if (result.hasErrors()) {
            return "/market/marketInfo";
        }
        marketService.marketInfoRegister(marketInfoRequest.marketInfoRequest());
        return "redirect:/market/menu";
    }


    // 가게 이름 중복검사
    @GetMapping("/marketName/check")
    public ResponseEntity<Boolean> checkMarketNameDuplicate(@RequestParam(value="marketName") String marketName) {
        boolean isAvailable = marketService.checkMarketNameDuplicate(marketName.trim());
        return ResponseEntity.ok(isAvailable);
    }

    @GetMapping("/menu")
    public String marketMenu() {
        return "/market/marketMenu";
    }
    @PostMapping("/menu/save")
    public String saveMenu(@ModelAttribute("menuCategory")MarketMenuCategoryRequest marketMenuCategoryRequest
            ,@ModelAttribute("menu")MarketMenuRequest marketMenuRequest) {
        List<MarketMenuCategoryRequest> requests = Arrays.asList((MarketMenuCategoryRequest) marketMenuCategoryRequest.getMenuCategories());
        System.out.println(requests);
        return "redirect:/market/marketMenu";
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
