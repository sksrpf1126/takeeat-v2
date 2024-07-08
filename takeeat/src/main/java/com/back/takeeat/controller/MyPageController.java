package com.back.takeeat.controller;

import com.back.takeeat.common.exception.AccessDeniedException;
import com.back.takeeat.dto.myPage.request.ReviewFormRequest;
import com.back.takeeat.dto.myPage.response.OrderDetailResponse;
import com.back.takeeat.dto.myPage.response.OrderListResponse;
import com.back.takeeat.service.MyPageService;
import com.back.takeeat.service.ReviewService;
import com.back.takeeat.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/my")
public class MyPageController {

    private final MyPageService myPageService;
    private final ReviewService reviewService;
    private final S3Service s3Service;

    @GetMapping("/order/list")
    public String orderList(Model model) {
        Long memberId = 1L; //(임시)로그인 회원

        List<OrderListResponse> orderListResponse = myPageService.getOrderList(memberId);

        model.addAttribute("orderListResponse", orderListResponse);
        return "myPage/orderList";
    }

    @GetMapping("/{orderId}/order")
    public String orderDetail(@PathVariable("orderId") Long orderId, Model model) {

        OrderDetailResponse orderDetailResponse = myPageService.getOrderDetail(orderId);

        model.addAttribute("orderDetailResponse", orderDetailResponse);
        return "myPage/orderDetail";
    }

    @GetMapping("/review/new")
    public String write(@RequestParam("orderId") Long orderId, Model model) {
        Long memberId = 1L; //(임시)로그인 회원

        String marketName = null;
        try {
            marketName = myPageService.getOrderMarketName(memberId, orderId);
        } catch (AccessDeniedException e) {
            return "errorPage/noAuthorityPage";
        }

        model.addAttribute("marketName", marketName);
        return "myPage/reviewForm";
    }

    @ResponseBody
    @PostMapping("/review/write")
    public ResponseEntity<String> write(@ModelAttribute ReviewFormRequest reviewFormRequest) {

        System.out.println(reviewFormRequest.getOrderId());
        //빈 파일이 폼 데이터에 포함되어 실제 파일이 업로드되었는지 확인
        List<String> imgUrls = new ArrayList<>();
        if (reviewFormRequest.getFile() != null) {
            List<MultipartFile> validFiles = reviewFormRequest.getFile().stream()
                    .filter(file -> file != null && !file.isEmpty())
                    .collect(Collectors.toList());

            if (!validFiles.isEmpty()) {
                imgUrls = s3Service.uploadFile(reviewFormRequest.getFile());
            }
        }

        reviewService.write(reviewFormRequest.getOrderId(), reviewFormRequest.getRating(),
                reviewFormRequest.getContent(), imgUrls);

        return ResponseEntity.ok("리뷰 작성 성공");
    }

    @GetMapping("/review/list")
    public String reviewList() {
        return "myPage/reviewList";
    }
}
