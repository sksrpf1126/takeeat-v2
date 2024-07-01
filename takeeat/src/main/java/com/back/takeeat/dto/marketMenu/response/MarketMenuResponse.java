package com.back.takeeat.dto.marketMenu.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketMenuResponse {

    private MarketResponse marketResponse;
    private List<MenuResponse> menuResponses;

    public static MarketMenuResponse createMarketMenuResponse(MarketResponse marketResponse, List<MenuResponse> menuResponses) {
        return MarketMenuResponse.builder()
                .marketResponse(marketResponse)
                .menuResponses(menuResponses)
                .build();
    }
}
