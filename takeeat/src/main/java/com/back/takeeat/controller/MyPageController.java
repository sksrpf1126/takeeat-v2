package com.back.takeeat.controller;

import com.back.takeeat.common.exception.AccessDeniedException;
import com.back.takeeat.dto.myPage.response.OrderDetailResponse;
import com.back.takeeat.dto.myPage.response.OrderListResponse;
import com.back.takeeat.service.MyPageService;
import com.back.takeeat.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/my")
public class MyPageController {

    private final MyPageService myPageService;
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

    @PostMapping("/review/write")
    public String write(@RequestParam("file") List<MultipartFile> file) {
        List<String> imgUrls = s3Service.uploadFile(file);
        System.out.println(imgUrls);
        return "redirect:/my/order/list";
    }

    @GetMapping("/review/list")
    public String reviewList() {
        return "myPage/reviewList";
    }
}
