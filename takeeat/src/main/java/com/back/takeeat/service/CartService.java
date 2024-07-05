package com.back.takeeat.service;

import com.back.takeeat.common.exception.OtherMarketMenuException;
import com.back.takeeat.domain.cart.Cart;
import com.back.takeeat.domain.cart.CartMenu;
import com.back.takeeat.domain.cart.CartOption;
import com.back.takeeat.domain.market.Market;
import com.back.takeeat.domain.menu.Menu;
import com.back.takeeat.domain.option.Option;
import com.back.takeeat.dto.cart.CartMenuIdAndOptionCategoryId;
import com.back.takeeat.dto.cart.request.AddToCartRequest;
import com.back.takeeat.dto.cart.response.CartListResponse;
import com.back.takeeat.dto.cart.response.CartMenuResponse;
import com.back.takeeat.dto.cart.response.CartOptionCategoryResponse;
import com.back.takeeat.dto.cart.response.CartOptionResponse;
import com.back.takeeat.repository.*;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final EntityManager em;

    private final MarketRepository marketRepository;
    private final MenuRepository menuRepository;
    private final OptionRepository optionRepository;
    private final CartRepository cartRepository;
    private final CartMenuRepository cartMenuRepository;
    private final CartOptionRepository cartOptionRepository;

    @Transactional(readOnly = true)
    public CartListResponse getList(Long memberId) {

        Cart cart = cartRepository.findByMemberIdWithMenu(memberId);
        if (cart == null) {
            throw new NoSuchElementException();
        }

        //장바구니에 담긴 메뉴 관련 정보
        List<CartMenuResponse> cartMenuResponses = new ArrayList<>();
        for (CartMenu cartMenu : cart.getCartMenus()) {
            cartMenuResponses.add(CartMenuResponse.create(cartMenu.getId(), cartMenu.getCartQuantity(),
                    cartMenu.getCartMenuPrice(), cartMenu.getMenu().getMenuName()));
        }

        //장바구니에 담긴 메뉴별 OptionCategory
        List<CartOptionCategoryResponse> cartOptionCategoryResponses = cartMenuRepository.findByCartIdWithOptionCategory(cart.getId());
        Map<Long, List<CartOptionCategoryResponse>> optionCategoryByCartMenu = cartOptionCategoryResponses.stream()
                .collect(Collectors.groupingBy(CartOptionCategoryResponse::getCartMenuId));

        //(CartMenuId, OptionCategory)별 선택된 Option 이름
        List<CartOptionResponse> cartOptionResponses = cartMenuRepository.findByCartIdWithOption(cart.getId());
        Map<CartMenuIdAndOptionCategoryId, List<CartOptionResponse>> cartOptionMapByOptionCategoryId = cartOptionResponses.stream()
                .collect(Collectors.groupingBy(cartOptionResponse ->
                        new CartMenuIdAndOptionCategoryId(
                                cartOptionResponse.getCartMenuId(),
                                cartOptionResponse.getOptionCategoryId()
                        )
                ));

        return CartListResponse.create((cart.getMarket() == null? null : cart.getMarket().getId()), (cart.getMarket() == null? null : cart.getMarket().getMarketName()),
                cartMenuResponses, optionCategoryByCartMenu, cartOptionMapByOptionCategoryId);
    }

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
            cart.addFirstMenu(market);
        }

        //메뉴 저장
        Menu menu = menuRepository.findById(addToCartRequest.getMenuId())
                .orElseThrow(NoSuchElementException::new);
        CartMenu cartMenu = cartMenuRepository.save(addToCartRequest.toCartMenu(cart, menu));

        //옵션 저장
        List<Option> options = optionRepository.findAllById(addToCartRequest.getOptionIds());
        List<CartOption> cartOptions = options.stream()
                .map(option -> addToCartRequest.toCartOption(cartMenu, option))
                .collect(Collectors.toList());
        cartOptionRepository.saveAll(cartOptions);

    }

    public void deleteAndAdd(AddToCartRequest addToCartRequest) {

        //기존 장바구니 삭제
        Cart cart = cartRepository.findByMemberId(addToCartRequest.getMemberId());
        cartMenuRepository.deleteByCartId(cart.getId());

        //가게 저장
        Market market = marketRepository.findById(addToCartRequest.getMarketId())
                .orElseThrow(NoSuchElementException::new);
        cart.addFirstMenu(market);

        //메뉴 저장
        Menu menu = menuRepository.findById(addToCartRequest.getMenuId())
                .orElseThrow(NoSuchElementException::new);
        CartMenu cartMenu = cartMenuRepository.save(addToCartRequest.toCartMenu(cart, menu));

        //옵션 저장
        List<Option> options = optionRepository.findAllById(addToCartRequest.getOptionIds());
        List<CartOption> cartOptions = options.stream()
                .map(option -> addToCartRequest.toCartOption(cartMenu, option))
                .collect(Collectors.toList());
        cartOptionRepository.saveAll(cartOptions);
    }

    public void updateQuantity(Long cartMenuId, int quantity) {

        CartMenu cartMenu = cartMenuRepository.findById(cartMenuId)
                .orElseThrow(NoSuchElementException::new);

        cartMenu.updateQuantity(quantity);
    }

    public int deleteCartMenu(Long cartMenuId) {

        CartMenu cartMenu = cartMenuRepository.findById(cartMenuId)
                .orElseThrow(NoSuchElementException::new);

        cartMenuRepository.delete(cartMenu);

        em.flush();

        //더 이상 menu가 없다면 cart 초기화
        Cart cart = cartRepository.findById(cartMenu.getCart().getId())
                .orElseThrow(NoSuchElementException::new);

        if (cart.getCartMenus().isEmpty()) {
            cart.deleteLastMenu();
            return 0;
        }

        return cart.getCartMenus().size();
    }

    public void deleteAllCartMenu(Long memberId) {

        Cart cart = cartRepository.findByMemberId(memberId);

        cartMenuRepository.deleteByCartId(cart.getId());

        cart.deleteLastMenu();
    }
}
