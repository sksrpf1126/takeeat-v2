package com.back.takeeat.controller;

import com.back.takeeat.common.exception.OtherMarketMenuException;
import com.back.takeeat.domain.user.Member;
import com.back.takeeat.dto.cart.request.AddToCartRequest;
import com.back.takeeat.dto.cart.response.CartListResponse;
import com.back.takeeat.security.LoginMember;
import com.back.takeeat.service.CartService;
import lombok.RequiredArgsConstructor;
import org.apache.el.parser.BooleanNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/cart")
    public String cart(@LoginMember Member member, Model model) {
        Long memberId = member.getId();

        CartListResponse cartListResponse = cartService.getList(memberId);

        model.addAttribute("cartListResponse", cartListResponse);
        return "order/cart";
    }

    @ResponseBody
    @PostMapping("/addToCart")
    public ResponseEntity<String> addToCart(@LoginMember Member member, @RequestBody Map<String, Object> cartData) {
        Long marketId = ((Integer)cartData.get("marketId")).longValue();
        Long menuId = ((Integer)cartData.get("menuId")).longValue();
        List<Long> optionIds = new ArrayList<>();
        List<?> optionIdsOfCartData =  (List<?>) cartData.get("optionIds");
        for (Object optionId : optionIdsOfCartData) {
            if (optionId instanceof Integer) {
                optionIds.add(((Integer) optionId).longValue());
            }
        }
        int quantity = (int)cartData.get("quantity");
        int cartMenuPrice = (int)cartData.get("cartMenuPrice");

        Long memberId = member.getId();

        try {
            cartService.add(new AddToCartRequest(memberId, marketId, menuId, quantity, cartMenuPrice, optionIds));
        } catch (OtherMarketMenuException e) {
            //다른 가게 메뉴가 장바구니에 이미 담겨있는 경우
            return ResponseEntity.status(HttpStatus.CONFLICT).body("다른 가게의 메뉴가 이미 장바구니에 있습니다.");
        }

        return ResponseEntity.ok("장바구니 추가 성공");
    }

    @ResponseBody
    @PostMapping("deleteAndAddToCart")
    public ResponseEntity<String> deleteAndAddToCart(@LoginMember Member member, @RequestBody Map<String, Object> cartData) {
        Long marketId = ((Integer)cartData.get("marketId")).longValue();
        Long menuId = ((Integer)cartData.get("menuId")).longValue();
        List<Long> optionIds = new ArrayList<>();
        List<?> optionIdsOfCartData =  (List<?>) cartData.get("optionIds");
        for (Object optionId : optionIdsOfCartData) {
            if (optionId instanceof Integer) {
                optionIds.add(((Integer) optionId).longValue());
            }
        }
        int quantity = (int)cartData.get("quantity");
        int cartMenuPrice = (int)cartData.get("cartMenuPrice");

        Long memberId = member.getId();

        cartService.deleteAndAdd(new AddToCartRequest(memberId, marketId, menuId, quantity, cartMenuPrice, optionIds));

        return ResponseEntity.ok("장바구니 추가 성공");
    }

    @ResponseBody
    @PostMapping("/updateQuantity")
    public ResponseEntity<String> updateQuantity(@RequestBody Map<String, Object> cartData) {
        Long cartMenuId = ((Integer)cartData.get("cartMenuId")).longValue();
        int quantity = (int)cartData.get("quantity");

        cartService.updateQuantity(cartMenuId, quantity);

        return ResponseEntity.ok("수량 업데이트 성공");
    }

    @ResponseBody
    @PostMapping("/deleteCartMenu")
    public ResponseEntity<Map<String, Object>> deleteCartMenu(@RequestBody Map<String, Object> cartData) {
        Long cartMenuId = ((Integer)cartData.get("cartMenuId")).longValue();

        int cartSize = cartService.deleteCartMenu(cartMenuId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "장바구니 메뉴 삭제 성공");
        response.put("cartSize", cartSize);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/deleteAllCartMenu")
    public String deleteAllCartMenu(@LoginMember Member member) {
        Long memberId = member.getId();

        cartService.deleteAllCartMenu(memberId);

        return "redirect:/cart";
    }

    @ResponseBody
    @PostMapping("/checkCart")
    public ResponseEntity<Map<String, Boolean>> checkCart(@LoginMember Member member) {
        Long memberId = member.getId();

        Map<String, Boolean> response = new HashMap<>();

        int cartStatus = cartService.checkCart(memberId);

        if (cartStatus == 1) {
            response.put("isEmpty", true);
            return ResponseEntity.ok(response);
        } else if (cartStatus == 2) {
            response.put("closed", true);
            return ResponseEntity.ok(response);
        } else {
            response.put("isEmpty", false);
            return ResponseEntity.ok(response);
        }
    }
}
