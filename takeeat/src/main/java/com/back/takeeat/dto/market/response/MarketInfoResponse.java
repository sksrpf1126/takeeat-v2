package com.back.takeeat.dto.market.response;

import com.back.takeeat.domain.market.Market;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MarketInfoResponse {
    private String marketName; // 가게 이름

    private String marketImage; // 가게 사진

    private String query; // 도로명 주소

    private String addressDetail; // 상세 주소

    private String businessNumber; // 사업자 번호

    private String marketNumber; // 가게 전화번호

    private String marketCategory; // 가게 카테고리

    private String marketIntroduction; // 가게 소개글

    private String operationTime; // 운영시간

    private String closedDays; // 휴무일

    public static MarketInfoResponse of(Market market) {
        return MarketInfoResponse.builder()
                .marketName(market.getMarketName())
                .marketImage(market.getMarketImage())
                .query(market.getQuery())
                .addressDetail(market.getAddressDetail())
                .businessNumber(market.getBusinessNumber())
                .marketNumber(market.getMarketNumber())
                .marketCategory(market.getMarketCategory())
                .marketIntroduction(market.getMarketIntroduction())
                .operationTime(market.getOperationTime())
                .closedDays(market.getClosedDays())
                .build();
    }

}