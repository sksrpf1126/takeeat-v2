package com.back.takeeat.dto.mainPage.response;

import com.back.takeeat.domain.market.Market;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketInfoResponse {
    private Long marketId;
    private String marketName;
    private String marketImage;
    private String query;
    private String addressDetail;
    private Double latitude;
    private Double longitude;
    private Double marketRating;
    private int reviewCount;

    public static MarketInfoResponse of(Market market) {
        return MarketInfoResponse.builder()
                .marketId(market.getId())
                .marketName(market.getMarketName())
                .marketImage(market.getMarketImage())
                .query(market.getQuery())
                .addressDetail(market.getAddressDetail())
                .latitude(market.getLatitude())
                .longitude(market.getLongitude())
                .marketRating(market.getMarketRating())
                .reviewCount(market.getReviewCount())
                .build();
    }
}
