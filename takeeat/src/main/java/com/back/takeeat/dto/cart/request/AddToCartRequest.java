package com.back.takeeat.dto.cart.request;

import com.back.takeeat.domain.cart.Cart;
import com.back.takeeat.domain.cart.CartMenu;
import com.back.takeeat.domain.cart.CartOption;
import com.back.takeeat.domain.market.Market;
import com.back.takeeat.domain.menu.Menu;
import com.back.takeeat.domain.option.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AddToCartRequest {

    Long memberId;
    Long marketId;

    Long menuId;
    int cartQuantity;
    int cartMenuPrice;

    List<Long> optionIds;

    public Cart toCart(Cart cart, Market market) {
        return Cart.builder()
                .id(cart.getId())
                .member(cart.getMember())
                .market(market)
                .build();
    }

    public CartMenu toCartMenu(Cart cart, Menu menu) {
        return CartMenu.builder()
                .cartQuantity(cartQuantity)
                .cartMenuPrice(cartMenuPrice)
                .cart(cart)
                .menu(menu)
                .build();
    }

    public CartOption toCartOption(CartMenu cartMenu, Option option) {
        return CartOption.builder()
                .cartMenu(cartMenu)
                .option(option)
                .build();
    }
}
