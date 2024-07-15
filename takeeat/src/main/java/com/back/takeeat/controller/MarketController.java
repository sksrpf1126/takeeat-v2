package com.back.takeeat.controller;


import com.back.takeeat.domain.menu.MenuCategory;
import com.back.takeeat.domain.user.Member;
import com.back.takeeat.dto.market.request.MarketInfoRequest;
import com.back.takeeat.dto.market.request.MarketOptionCategoryRequest;
import com.back.takeeat.dto.market.request.MenuRequest;
import com.back.takeeat.dto.market.request.OptionRequest;
import com.back.takeeat.dto.market.response.MarketReviewResponse;
import com.back.takeeat.dto.market.response.MenuCategoryNameResponse;
import com.back.takeeat.security.LoginMember;
import com.back.takeeat.service.MarketService;
import com.back.takeeat.service.ReviewService;
import com.back.takeeat.service.S3Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/market")
public class MarketController {

    private final MarketService marketService;
    private final ReviewService reviewService;
    private final S3Service s3Service;

    @Value("${KAKAO_API_KEY}")
    String KAKAO_API_KEY;

    @GetMapping("/info")
    public String marketInfo(@ModelAttribute("marketInfo") MarketInfoRequest marketInfoRequest, Model model) {
        model.addAttribute("marketInfo", marketInfoRequest);
        model.addAttribute("KAKAO_API_KEY", KAKAO_API_KEY);
        return "market/marketInfo";
    }


    @PostMapping("/info/save")
    public String saveMarketInfo(@Valid @ModelAttribute("marketInfo") MarketInfoRequest marketInfoRequest
                                ,BindingResult result
                                ,Model model) {
        List<String> imgUrls = new ArrayList<>();
        if (result.hasErrors()) {
            return "market/marketInfo";
        }
        Long memberId = 1L;
        if(marketInfoRequest.getMarketImage() != null) {
            List<MultipartFile> validFiles = marketInfoRequest.getMarketImage().stream()
                    .filter(file -> file != null && !file.isEmpty())
                    .collect(Collectors.toList());

            if (!validFiles.isEmpty()) {
                imgUrls = s3Service.uploadFile(marketInfoRequest.getMarketImage());
            }
        }
        marketService.marketInfoRegister(marketInfoRequest.create(), memberId, imgUrls);
        model.addAttribute("KAKAO_API_KEY", KAKAO_API_KEY);
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
        return "market/marketMenu";
    }

    @PostMapping("/menu/save")
    @ResponseBody
    public ResponseEntity<String> saveMenu(@RequestBody @Valid MenuRequest menuRequest, BindingResult result) {
        List<String> imgUrls = new ArrayList<>();
        if (result.hasErrors()) {
            String errorMessages = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body("{\"message\": \"메뉴 저장 실패: " + errorMessages + "\"}");
        }
        Long memberId = 2L;

        if(menuRequest.getMenuImage() != null) {
            List<MultipartFile> validFiles = menuRequest.getMenuImage().stream()
                    .filter(file -> file != null && !file.isEmpty())
                    .collect(Collectors.toList());

            if (!validFiles.isEmpty()) {
                imgUrls = s3Service.uploadFile(menuRequest.getMenuImage());
            }
        }

        try {
            marketService.menuCategoriesRegister(menuRequest, memberId, imgUrls);
            return ResponseEntity.ok("{\"message\": \"메뉴 저장 성공\"}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("{\"message\": \"메뉴 저장 실패\"}");
        }
    }

    @GetMapping("/menus")
    @ResponseBody
    public List<MenuCategoryNameResponse> getMenus() {
        Long memberId = 2L;
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
    public ResponseEntity<String> saveOption(@ModelAttribute("optionRequest") OptionRequest optionRequest) {

        Long memberId = 2L;
        System.out.println("menuCategory=" + optionRequest);
        try {
            marketService.optionCategoriesRegister(memberId, optionRequest.getMarketOptionCategoryRequestList());
            return ResponseEntity.ok("{\"message\": \"옵션 저장 성공\"}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("{\"message\": \"옵션 저장 실패\"}");
        }
    }

    @GetMapping("/review")
    public String marketReview(@LoginMember Member member, Model model) {
        Long memberId = member.getId();

        MarketReviewResponse marketReviewResponse = marketService.getReviewInfo(memberId);

        model.addAttribute("marketReviewResponse", marketReviewResponse);
        return "market/marketReview";
    }

    @ResponseBody
    @PostMapping("/review/write")
    public ResponseEntity<String> saveOwnerReview(@RequestBody Map<String, Object> reviewData) {
        Long reviewId = ((Integer)reviewData.get("reviewId")).longValue();
        String ownerReviewContent = (String) reviewData.get("ownerReviewContent");

        String task = reviewService.writeOwnerReview(reviewId, ownerReviewContent);

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

    @ResponseBody
    @PostMapping("/review/report")
    public ResponseEntity<String> reportReview(@RequestBody Map<String, Object> reviewData) {
        Long reviewId = ((Integer)reviewData.get("reviewId")).longValue();

        reviewService.reportReview(reviewId);

        return ResponseEntity.ok("리뷰 신고 완료");
    }

}
