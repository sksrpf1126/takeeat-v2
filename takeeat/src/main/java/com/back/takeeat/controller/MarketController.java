package com.back.takeeat.controller;


import com.back.takeeat.dto.market.request.MarketInfoRequest;
import com.back.takeeat.dto.market.request.MenuRequest;
import com.back.takeeat.dto.market.response.MarketReviewResponse;
import com.back.takeeat.dto.myPage.request.ReviewFormRequest;
import com.back.takeeat.service.MarketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    @ResponseBody
    public ResponseEntity<String> saveMenuCategory(@RequestBody @Valid MenuRequest menuRequest) {
        try {
            marketService.MenuCategoriesRegister(menuRequest);
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

    @GetMapping("/review")
    public String marketReview(Model model) {
        Long memberId = 1L; //(임시)로그인 회원

        MarketReviewResponse marketReviewResponse = marketService.getReviewInfo(memberId);

        model.addAttribute("marketReviewResponse", marketReviewResponse);
        return "market/marketReview";
    }

    @ResponseBody
    @PostMapping("/review/write")
    public ResponseEntity<String> saveOwnerReview(@RequestBody Map<String, Object> reviewData) {
        Long reviewId = ((Integer)reviewData.get("reviewId")).longValue();
        String ownerReviewContent = (String) reviewData.get("ownerReviewContent");

        String task = marketService.saveOwnerReview(reviewId, ownerReviewContent);

        switch (task) {
            case "modify":
                return ResponseEntity.ok("답글이 수정되었습니다");
            case "delete":
                return ResponseEntity.ok("답글이 삭제되었습니다");
            case "write":
                return ResponseEntity.ok("답글이 작성되었습니다");
            case "none":
                return ResponseEntity.ok("none");
        }

        return ResponseEntity.ok("none");
    }

}
