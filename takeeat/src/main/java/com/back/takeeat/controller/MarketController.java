package com.back.takeeat.controller;


import com.back.takeeat.dto.market.request.MarketInfoRequest;
import com.back.takeeat.dto.market.request.MenuRequest;
import com.back.takeeat.dto.market.request.OptionRequest;
import com.back.takeeat.dto.market.response.MenuCategoryNameResponse;
import com.back.takeeat.service.MarketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/market")
public class MarketController {

    private final MarketService marketService;

    @GetMapping("/info")
    public String marketInfo(@ModelAttribute("marketInfo") MarketInfoRequest marketInfoRequest, Model model) {

        model.addAttribute("marketInfoRequest", marketInfoRequest);

        return "market/marketInfo";
    }


    @PostMapping("/info/save")
    public String saveMarketInfo(@Valid @ModelAttribute("marketInfo") MarketInfoRequest marketInfoRequest
                                ,BindingResult result) {
        if (result.hasErrors()) {
            return "market/marketInfo";
        }
        Long memberId = 1L;
        marketService.marketInfoRegister(marketInfoRequest.create(), memberId);
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
    @ResponseBody
    public ResponseEntity<String> saveMenu(@RequestBody @Valid MenuRequest menuRequest, BindingResult result) {
        if (result.hasErrors()) {
            String errorMessages = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body("{\"message\": \"메뉴 저장 실패: " + errorMessages + "\"}");
        }
        Long memberId = 1L;
        try {
            marketService.menuCategoriesRegister(menuRequest, memberId);
            return ResponseEntity.ok("{\"message\": \"메뉴 저장 성공\"}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("{\"message\": \"메뉴 저장 실패\"}");
        }
    }

    @GetMapping("/menus")
    @ResponseBody
    public List<MenuCategoryNameResponse> getMenus() {
        Long memberId = 1L;
        List<MenuCategoryNameResponse> menuResponses = marketService.getMarketMenuName(memberId);
        for(MenuCategoryNameResponse menuCategoryNameResponse : menuResponses) {
            System.out.println(menuCategoryNameResponse.getMenuName());
        }
        return menuResponses;
    }

    @GetMapping("/option")
    public String marketOption() {
        return "market/marketOption";
    }

    @PostMapping("/option/save")
    @ResponseBody
    public ResponseEntity<String> saveOption(@RequestBody OptionRequest optionRequest) {
        try {
            marketService.optionCategoriesRegister(optionRequest);
            return ResponseEntity.ok("{\"message\": \"옵션 저장 성공\"}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("{\"message\": \"옵션 저장 실패\"}");
        }
    }

    @GetMapping("/review")
    public String marketReview() {
        return "market/marketReview";
    }

}
