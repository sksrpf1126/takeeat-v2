package com.back.takeeat.dto.cart.response;

import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public class CartListResponse {

    String marketName;

    List<CartMenuResponse> cartMenuResponses;

    Map<Long, List<CartOptionResponse>> cartOptionMapByCartMenuId;
    Map<Long, List<CartOptionResponse>> cartOptionMapByOptionCategoryId;

    public static CartListResponse create(String marketName, List<CartMenuResponse> cartMenuResponses,
                                          Map<Long, List<CartOptionResponse>> cartOptionMapByCartMenuId,
                                          Map<Long, List<CartOptionResponse>> cartOptionMapByOptionCategoryId) {
        return CartListResponse.builder()
                .marketName(marketName)
                .cartMenuResponses(cartMenuResponses)
                .cartOptionMapByCartMenuId(cartOptionMapByCartMenuId)
                .cartOptionMapByOptionCategoryId(cartOptionMapByOptionCategoryId)
                .build();
    }
}
