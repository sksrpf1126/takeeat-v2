package com.back.takeeat.service;

import com.back.takeeat.common.exception.EntityNotFoundException;
import com.back.takeeat.common.exception.ErrorCode;
import com.back.takeeat.common.exception.OtherMarketMenuException;
import com.back.takeeat.domain.cart.Cart;
import com.back.takeeat.domain.cart.CartMenu;
import com.back.takeeat.domain.cart.CartOption;
import com.back.takeeat.domain.market.Market;
import com.back.takeeat.domain.market.MarketStatus;
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

        Cart cart = cartRepository.findByMemberIdWithMenu(memberId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.CART_NOT_FOUND));

        //장바구니에 담긴 메뉴 관련 정보
        List<CartMenuResponse> cartMenuResponses = getCartMenuResponses(cart);

        //장바구니에 담긴 메뉴별 OptionCategory
        Map<Long, List<CartOptionCategoryResponse>> optionCategoryByCartMenu = getOptionCategoryByCartMenu(cart);

        //(CartMenuId, OptionCategory)별 선택된 Option 이름
        Map<CartMenuIdAndOptionCategoryId, List<CartOptionResponse>> cartOptionMapByOptionCategoryId = getCartOptionMapByOptionCategoryId(cart);

        return CartListResponse.create((cart.getMarket() == null? null : cart.getMarket().getId()), (cart.getMarket() == null? null : cart.getMarket().getMarketName()),
                (cart.getMarket() == null? null : cart.getMarket().getMarketStatus()), cartMenuResponses, optionCategoryByCartMenu, cartOptionMapByOptionCategoryId);
    }

    public List<CartMenuResponse> getCartMenuResponses(Cart cart) {
        List<CartMenuResponse> cartMenuResponses = new ArrayList<>();
        for (CartMenu cartMenu : cart.getCartMenus()) {
            cartMenuResponses.add(CartMenuResponse.create(cartMenu.getId(), cartMenu.getCartQuantity(),
                    cartMenu.getCartMenuPrice(), cartMenu.getMenu().getId(), cartMenu.getMenu().getMenuName(), cartMenu.getMenu().getMenuPrice()));
        }
        return cartMenuResponses;
    }

    public Map<Long, List<CartOptionCategoryResponse>> getOptionCategoryByCartMenu(Cart cart) {
        List<CartOptionCategoryResponse> cartOptionCategoryResponses = cartMenuRepository.findByCartIdWithOptionCategory(cart.getId());
        Map<Long, List<CartOptionCategoryResponse>> optionCategoryByCartMenu = cartOptionCategoryResponses.stream()
                .collect(Collectors.groupingBy(CartOptionCategoryResponse::getCartMenuId));
        return optionCategoryByCartMenu;
    }

    public Map<CartMenuIdAndOptionCategoryId, List<CartOptionResponse>> getCartOptionMapByOptionCategoryId(Cart cart) {
        List<CartOptionResponse> cartOptionResponses = cartMenuRepository.findByCartIdWithOption(cart.getId());
        Map<CartMenuIdAndOptionCategoryId, List<CartOptionResponse>> cartOptionMapByOptionCategoryId = cartOptionResponses.stream()
                .collect(Collectors.groupingBy(cartOptionResponse ->
                        new CartMenuIdAndOptionCategoryId(
                                cartOptionResponse.getCartMenuId(),
                                cartOptionResponse.getOptionCategoryId()
                        )
                ));
        return cartOptionMapByOptionCategoryId;
    }

    public void add(AddToCartRequest addToCartRequest) throws OtherMarketMenuException {

        Cart cart = cartRepository.findByMemberId(addToCartRequest.getMemberId());
        //다른 가게의 메뉴가 이미 장바구니에 담겨 있는 경우
        if (cart.getMarket() != null && !cart.getMarket().getId().equals(addToCartRequest.getMarketId())) {
            throw new OtherMarketMenuException();
        }

        Market market = marketRepository.findById(addToCartRequest.getMarketId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MARKET_NOT_FOUND));

        //장바구니에 첫 번째 메뉴를 담았다면 가게 정보 저장
        if (cart.getMarket() == null) {
            cart.addFirstMenu(market);
        }

        //새로 담은 메뉴와 동일한 메뉴의 정보 가져오기
        List<CartMenu> cartMenus = cartMenuRepository.findAllByMenuId(addToCartRequest.getMenuId());

        //담은 메뉴의 메뉴
        Menu menu = menuRepository.findById(addToCartRequest.getMenuId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MENU_NOT_FOUND));

        //담은 메뉴의 옵션
        List<Option> options = optionRepository.findAllById(addToCartRequest.getOptionIds());

        boolean alreadyHaveMenu = false; //새로 다음 메뉴가 이미 장바구니에 담겨있는지 체크하는 변수
        CartMenu updateCartMenu = null; //수량을 업데이트 해야하는 CartMenu
        //같은 옵션이 선택된 메뉴인지 체크하기 위한 반복문
        for (CartMenu cartMenu : cartMenus) {
            //사이즈가 다르다면 이미 같은 옵션의 메뉴가 아님
            if (options.size() == cartMenu.getCartOptions().size()) {
                //옵션이 없는 메뉴
                if (options.isEmpty()) {
                    updateCartMenu = cartMenu;
                    alreadyHaveMenu = true;
                    break;
                }

                //같은 옵션의 메뉴인지 체크
                int idx = 0;
                int sameItems = 0;
                for (CartOption cartOption : cartMenu.getCartOptions()) {
                    if (options.get(idx).getId().equals(cartOption.getOption().getId())) {
                        sameItems++;
                        idx++;
                        continue;
                    }
                }
                if (sameItems == options.size()) {
                    updateCartMenu = cartMenu;
                    alreadyHaveMenu = true;
                }
            }
        }

        if (alreadyHaveMenu) {
            //메뉴 수량 업데이트
            updateCartMenu.addQuantity(addToCartRequest.getCartQuantity());
        } else {
            //메뉴 및 옵션 저장
            CartMenu cartMenu = cartMenuRepository.save(addToCartRequest.toCartMenu(cart, menu));
            List<CartOption> cartOptions = options.stream()
                    .map(option -> addToCartRequest.toCartOption(cartMenu, option))
                    .collect(Collectors.toList());
            cartOptionRepository.saveAll(cartOptions);
        }

    }

    public void deleteAndAdd(AddToCartRequest addToCartRequest) {

        //기존 장바구니 삭제
        Cart cart = cartRepository.findByMemberId(addToCartRequest.getMemberId());
        cartMenuRepository.deleteByCartId(cart.getId());

        //가게 저장
        Market market = marketRepository.findById(addToCartRequest.getMarketId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MARKET_NOT_FOUND));
        cart.addFirstMenu(market);

        //메뉴 저장
        Menu menu = menuRepository.findById(addToCartRequest.getMenuId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MENU_NOT_FOUND));
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
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.CARTMENU_NOT_FOUND));

        cartMenu.updateQuantity(quantity);
    }

    public int deleteCartMenu(Long cartMenuId) {

        CartMenu cartMenu = cartMenuRepository.findById(cartMenuId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.CARTMENU_NOT_FOUND));

        cartMenuRepository.delete(cartMenu);

        em.flush();

        //더 이상 menu가 없다면 cart 초기화
        Cart cart = cartRepository.findById(cartMenu.getCart().getId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.CART_NOT_FOUND));

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

    @Transactional(readOnly = true)
    public int checkCart(Long memberId) {

        Cart cart = cartRepository.findByMemberId(memberId);

        if (cart.getCartMenus() == null || cart.getCartMenus().isEmpty()) {
            //장바구니가 비어있음
            return 1;
        } else if (cart.getMarket().getMarketStatus() == MarketStatus.CLOSE) {
            //가게 준비중
            return 2;
        } else {
            //주문 가능
            return 3;
        }
    }
}
