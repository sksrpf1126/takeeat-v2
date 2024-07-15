package com.back.takeeat.dto.order.response;

import com.back.takeeat.domain.market.Market;
import com.back.takeeat.domain.user.Member;
import com.back.takeeat.dto.cart.CartMenuIdAndOptionCategoryId;
import com.back.takeeat.dto.cart.response.CartMenuResponse;
import com.back.takeeat.dto.cart.response.CartOptionCategoryResponse;
import com.back.takeeat.dto.cart.response.CartOptionResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class OrderResponse {

    private Long marketId;
    private String marketName;
    private String query;
    private String addressDetail;
    private double latitude;
    private double longitude;

    private String phone;

    private List<CartMenuResponse> cartMenuResponses;

    private Map<Long, List<CartOptionCategoryResponse>> optionCategoryByCartMenu;
    private Map<CartMenuIdAndOptionCategoryId, List<CartOptionResponse>> cartOptionMapByOptionCategoryId;

    public static OrderResponse create(Market market, Member member, List<CartMenuResponse> cartMenuResponses,
                                       Map<Long, List<CartOptionCategoryResponse>> optionCategoryByCartMenu,
                                       Map<CartMenuIdAndOptionCategoryId, List<CartOptionResponse>> cartOptionMapByOptionCategoryId) {
        return OrderResponse.builder()
                .marketId(market.getId())
                .marketName(market.getMarketName())
                .query(market.getQuery())
                .addressDetail(market.getAddressDetail())
                .latitude(market.getLatitude())
                .longitude(market.getLongitude())
                .phone(member.getPhone())
                .cartMenuResponses(cartMenuResponses)
                .optionCategoryByCartMenu(optionCategoryByCartMenu)
                .cartOptionMapByOptionCategoryId(cartOptionMapByOptionCategoryId)
                .build();
    }

    public List<CartOptionResponse> getCartOptionInfo(Long cartMenuId, Long optionCategoryId) {
        CartMenuIdAndOptionCategoryId cao = new CartMenuIdAndOptionCategoryId(cartMenuId, optionCategoryId);
        return cartOptionMapByOptionCategoryId.get(cao);
    }
}
