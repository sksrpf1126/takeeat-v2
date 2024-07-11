package com.back.takeeat.dto.mainPage.response;

import com.back.takeeat.domain.market.Market;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

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

    private Double minLatitude;

    private Double maxLatitude;

    private Double longitude;

    private Double minLongitude;

    private Double maxLongitude;

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
                .minLatitude(builder().minLatitude)
                .maxLatitude(builder().maxLatitude)
                .minLongitude(builder().minLongitude)
                .maxLongitude(builder().maxLongitude)
                .longitude(market.getLongitude())
                .marketRating(market.getMarketRating())
                .reviewCount(market.getReviewCount())
                .build();
    }

    // markets 리스트에서 특정 범위의 위치 정보를 가진 Market 객체들을 선택
    public static List<MarketInfoResponse> listOf(List<Market> markets, Double minLat, Double maxLat, Double minLon, Double maxLon) {
        return markets.stream()
                .filter(market -> market.getLatitude() >= minLat && market.getLatitude() <= maxLat &&
                        market.getLongitude() >= minLon && market.getLongitude() <= maxLon)
                .map(MarketInfoResponse::of)
                .collect(Collectors.toList());
    }
}
