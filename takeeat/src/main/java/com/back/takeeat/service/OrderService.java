package com.back.takeeat.service;

import com.back.takeeat.domain.cart.Cart;
import com.back.takeeat.dto.cart.CartMenuIdAndOptionCategoryId;
import com.back.takeeat.dto.cart.response.CartMenuResponse;
import com.back.takeeat.dto.cart.response.CartOptionCategoryResponse;
import com.back.takeeat.dto.cart.response.CartOptionResponse;
import com.back.takeeat.dto.order.response.OrderResponse;
import com.back.takeeat.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartService cartService;
    private final CartRepository cartRepository;

    public OrderResponse getOrderInfo(Long memberId) {

        Cart cart = cartRepository.findByMemberId(memberId);

        //장바구니에 담긴 메뉴 관련 정보
        List<CartMenuResponse> cartMenuResponses = cartService.getCartMenuResponses(cart);

        //장바구니에 담긴 메뉴별 OptionCategory
        Map<Long, List<CartOptionCategoryResponse>> optionCategoryByCartMenu = cartService.getOptionCategoryByCartMenu(cart);

        //(CartMenuId, OptionCategory)별 선택된 Option 이름
        Map<CartMenuIdAndOptionCategoryId, List<CartOptionResponse>> cartOptionMapByOptionCategoryId = cartService.getCartOptionMapByOptionCategoryId(cart);

        return OrderResponse.create(cart.getMarket(), cart.getMember(), cartMenuResponses, optionCategoryByCartMenu, cartOptionMapByOptionCategoryId);
    }
}
