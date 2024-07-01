package com.back.takeeat.dto.marketMenu.response;

import com.back.takeeat.domain.market.Market;
import com.back.takeeat.domain.market.MarketStatus;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketResponse {

    private Long marketId;
    private String marketName;
    private String marketImage;
    private String query;
    private String addressDetail;
    private Double latitude;
    private Double longitude;
    private String marketNumber;
    private String marketIntroduction;
    private String marketCategory;
    private String operationTime;
    private String businessNumber;
    private MarketStatus marketStatus;
    private Double marketRating;
    private int reviewCount;
    private String closedDays;

    public static MarketResponse createByMarket(Market market) {
        return MarketResponse.builder()
                .marketId(market.getId())
                .marketName(market.getMarketName())
                .marketImage(market.getMarketImage())
                .query(market.getQuery())
                .addressDetail(market.getAddressDetail())
                .latitude(market.getLatitude())
                .longitude(market.getLongitude())
                .marketNumber(market.getMarketNumber())
                .marketIntroduction(market.getMarketIntroduction())
                .marketCategory(market.getMarketCategory())
                .operationTime(market.getOperationTime())
                .businessNumber(market.getBusinessNumber())
                .marketStatus(market.getMarketStatus())
                .marketRating(market.getMarketRating())
                .reviewCount(market.getReviewCount())
                .closedDays(market.getClosedDays())
                .build();
    }

    public int getYellowStarNumber() {
        return (int)Math.floor(marketRating);
    }

    public int getGrayStarNumber() {
        return 5 - (int)Math.floor(marketRating);
    }
}
