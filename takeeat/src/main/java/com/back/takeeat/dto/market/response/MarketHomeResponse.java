package com.back.takeeat.dto.market.response;

import com.back.takeeat.domain.market.Market;
import com.back.takeeat.domain.market.MarketStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MarketHomeResponse {

    private String marketName;
    private String query;
    private String addressDetail;
    private String marketNumber;
    private String operationTime;
    private String businessNumber;
    private MarketStatus marketStatus;
    private String closedDays;

    public static MarketHomeResponse createByMarket(Market market) {
        return MarketHomeResponse.builder()
                .marketName(market.getMarketName())
                .query(market.getQuery())
                .addressDetail(market.getAddressDetail())
                .marketNumber(market.getMarketNumber())
                .operationTime(market.getOperationTime())
                .businessNumber(market.getBusinessNumber())
                .marketStatus(market.getMarketStatus())
                .closedDays(market.getClosedDays())
                .build();
    }
}
