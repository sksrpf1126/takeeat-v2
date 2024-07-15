package com.back.takeeat.controller;

import com.back.takeeat.domain.user.Member;
import com.back.takeeat.domain.user.MemberRoleType;
import com.back.takeeat.dto.myPage.request.ReviewFormRequest;
import com.back.takeeat.dto.myPage.request.ReviewModifyFormRequest;
import com.back.takeeat.dto.myPage.response.OrderDetailResponse;
import com.back.takeeat.dto.myPage.response.OrderListResponse;
import com.back.takeeat.dto.myPage.response.ReviewListResponse;
import com.back.takeeat.dto.myPage.response.ReviewModifyFormResponse;
import com.back.takeeat.security.LoginMember;
import com.back.takeeat.service.MyPageService;
import com.back.takeeat.service.ReviewService;
import com.back.takeeat.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/home")
    public String myPage(@LoginMember Member member, Model model) {
        String nickname = member.getNickname();

        model.addAttribute("nickname", nickname);
        return "myPage/myPage";
    }

    @GetMapping("/order/list")
    public String orderList(@LoginMember Member member, Model model) {
        Long memberId = member.getId();

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
    public String write(@LoginMember Member member, @RequestParam("orderId") Long orderId, Model model) {
        Long memberId = member.getId();

        String marketName = myPageService.getOrderMarketName(memberId, orderId);

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
    public String reviewList(@LoginMember Member member, Model model) {
        Long memberId = member.getId();

        List<ReviewListResponse> reviewListResponses = reviewService.getReviewList(memberId);

        model.addAttribute("reviewListResponses", reviewListResponses);
        return "myPage/reviewList";
    }

    @GetMapping("/review/modify")
    public String modify(@LoginMember Member member, @RequestParam("reviewId") Long reviewId, Model model) {
        Long memberId = member.getId();

        ReviewModifyFormResponse reviewModifyFormResponse = reviewService.getModifyForm(memberId, reviewId);

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
