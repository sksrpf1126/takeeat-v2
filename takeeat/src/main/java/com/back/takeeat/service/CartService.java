package com.back.takeeat.service;

import com.back.takeeat.common.exception.OtherMarketMenuException;
import com.back.takeeat.domain.cart.Cart;
import com.back.takeeat.domain.cart.CartMenu;
import com.back.takeeat.domain.cart.CartOption;
import com.back.takeeat.domain.market.Market;
import com.back.takeeat.domain.menu.Menu;
import com.back.takeeat.domain.option.Option;
import com.back.takeeat.dto.cart.request.AddToCartRequest;
import com.back.takeeat.dto.cart.response.CartListResponse;
import com.back.takeeat.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CartService {

    private final MarketRepository marketRepository;
    private final MenuRepository menuRepository;
    private final OptionRepository optionRepository;
    private final CartRepository cartRepository;
    private final CartMenuRepository cartMenuRepository;
    private final CartOptionRepository cartOptionRepository;

    @Transactional(readOnly = true)
    public CartListResponse getList(Long memberId) {

        Cart cart = cartRepository.findByMemberId(memberId);

        return null;
    }

    @Transactional
    public void add(AddToCartRequest addToCartRequest) throws OtherMarketMenuException {

        Cart cart = cartRepository.findByMemberId(addToCartRequest.getMemberId());
        //다른 가게의 메뉴가 이미 장바구니에 담겨 있는 경우
        if (cart.getMarket() != null && !cart.getMarket().getId().equals(addToCartRequest.getMarketId())) {
            throw new OtherMarketMenuException();
        }

        Market market = marketRepository.findById(addToCartRequest.getMarketId())
                .orElseThrow(NoSuchElementException::new);

        //장바구니에 첫 번째 메뉴를 담았다면 가게 정보 저장
        if (cart.getMarket() == null) {
            cart = addToCartRequest.toCart(cart, market);
            cartRepository.save(cart);
        }

        //메뉴 저장
        Menu menu = menuRepository.findById(addToCartRequest.getMenuId())
                .orElseThrow(NoSuchElementException::new);
        CartMenu cartMenu = cartMenuRepository.save(addToCartRequest.toCartMenu(cart, menu));

        //옵션 저장
        for (Long optionId : addToCartRequest.getOptionIds()) {
            Option option = optionRepository.findById(optionId)
                    .orElseThrow(NoSuchElementException::new);

            cartOptionRepository.save(addToCartRequest.toCartOption(cartMenu, option));
        }

    }
}
