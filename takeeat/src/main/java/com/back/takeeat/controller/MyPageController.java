package com.back.takeeat.controller;

import com.back.takeeat.common.exception.AccessDeniedException;
import com.back.takeeat.dto.myPage.request.ReviewFormRequest;
import com.back.takeeat.dto.myPage.request.ReviewModifyFormRequest;
import com.back.takeeat.dto.myPage.response.OrderDetailResponse;
import com.back.takeeat.dto.myPage.response.OrderListResponse;
import com.back.takeeat.dto.myPage.response.ReviewListResponse;
import com.back.takeeat.dto.myPage.response.ReviewModifyFormResponse;
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
import java.util.Map;
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

        //파일을 업로드 하지 않았을 때([[]]) -> 실제 파일이 업로드되었는지 확인하는 로직
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
    public String reviewList(Model model) {
        Long memberId = 1L; //(임시)로그인 회원

        List<ReviewListResponse> reviewListResponses = reviewService.getReviewList(memberId);

        model.addAttribute("reviewListResponses", reviewListResponses);
        return "myPage/reviewList";
    }

    @GetMapping("/review/modify")
    public String modify(@RequestParam("reviewId") Long reviewId, Model model) {
        Long memberId = 1L; //(임시)로그인 회원

        ReviewModifyFormResponse reviewModifyFormResponse = null;
        try {
            reviewModifyFormResponse = reviewService.getModifyForm(memberId, reviewId);
        } catch (AccessDeniedException e) {
            return "errorPage/noAuthorityPage";
        }

        model.addAttribute("reviewModifyFormResponse", reviewModifyFormResponse);
        return "myPage/reviewModifyForm";
    }

    @PostMapping("/review/modify")
    public ResponseEntity<String> modify(@ModelAttribute ReviewModifyFormRequest reviewModifyFormRequest) {

        //업로드 사진 변경 시: 기존 데이터 삭제 -> 새로운 데이터 저장
        if (reviewModifyFormRequest.getImageModifyFlag() == 1) {
            List<String> prevReviewImages = reviewService.getReviewImages(reviewModifyFormRequest.getReviewId());
            if (!prevReviewImages.isEmpty()) {
                s3Service.deleteFiles(prevReviewImages);
                reviewService.deleteReviewImages(reviewModifyFormRequest.getReviewId());
            }
        }

        //파일을 업로드 하지 않았을 때([[]]) -> 실제 파일이 업로드되었는지 확인하는 로직
        List<String> imgUrls = new ArrayList<>();
        if (reviewModifyFormRequest.getFile() != null) {
            List<MultipartFile> validFiles = reviewModifyFormRequest.getFile().stream()
                    .filter(file -> file != null && !file.isEmpty())
                    .collect(Collectors.toList());

            if (!validFiles.isEmpty()) {
                imgUrls = s3Service.uploadFile(reviewModifyFormRequest.getFile());
            }
        }

        reviewService.modify(reviewModifyFormRequest, imgUrls);

        return ResponseEntity.ok("리뷰 수정 성공");
    }

    @PostMapping("/review/delete")
    public ResponseEntity<String> delete(@RequestBody Map<String, Object> reviewData) {
        Long reviewId = ((Integer)reviewData.get("reviewId")).longValue();

        reviewService.delete(reviewId);

        return ResponseEntity.ok("리뷰 삭제 성공");
    }

}
