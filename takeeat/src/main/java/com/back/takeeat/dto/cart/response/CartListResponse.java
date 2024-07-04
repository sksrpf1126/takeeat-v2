package com.back.takeeat.dto.cart.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Builder
@Getter
public class CartListResponse {

    Long marketId;
    String marketName;

    List<CartMenuResponse> cartMenuResponses;

    Map<Long, List<CartOptionResponse>> cartOptionMapByCartMenuId;
    Map<Long, List<CartOptionResponse>> cartOptionMapByOptionCategoryId;

    public static CartListResponse create(Long marketId, String marketName, List<CartMenuResponse> cartMenuResponses,
                                          Map<Long, List<CartOptionResponse>> cartOptionMapByCartMenuId,
                                          Map<Long, List<CartOptionResponse>> cartOptionMapByOptionCategoryId) {
        return CartListResponse.builder()
                .marketId(marketId)
                .marketName(marketName)
                .cartMenuResponses(cartMenuResponses)
                .cartOptionMapByCartMenuId(cartOptionMapByCartMenuId)
                .cartOptionMapByOptionCategoryId(cartOptionMapByOptionCategoryId)
                .build();
    }
}
