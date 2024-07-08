package com.back.takeeat.controller;


import com.back.takeeat.dto.market.request.MarketInfoRequest;
import com.back.takeeat.dto.market.request.MenuRequest;
import com.back.takeeat.dto.market.request.OptionRequest;
import com.back.takeeat.dto.market.response.MarketMenuResponse;
import com.back.takeeat.service.MarketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

        return "/market/marketInfo";
    }


    @PostMapping("/info/save")
    public String saveMarketInfo(@Valid @ModelAttribute("marketInfo") MarketInfoRequest marketInfoRequest
                                ,BindingResult result) {
        if (result.hasErrors()) {
            return "/market/marketInfo";
        }
        Long memberId = 1L;
        marketService.marketInfoRegister(marketInfoRequest.marketInfoRequest(), memberId);
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

    @GetMapping("/option")
    public String marketOption() {
        return "/market/marketOption";
    }

    @GetMapping
    public ResponseEntity<List<MarketMenuResponse>> marketMenuName(Model model /*@RequestParam Long memberId*/) {
        Long memberId = 1L;
        try {
            List<MarketMenuResponse> menuResponses = marketService.getMarketMenuName(memberId);
            model.addAttribute("menuResponses", menuResponses);
            return ResponseEntity.ok(menuResponses);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
