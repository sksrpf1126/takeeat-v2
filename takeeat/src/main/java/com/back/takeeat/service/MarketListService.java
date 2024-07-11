package com.back.takeeat.service;

import com.back.takeeat.domain.market.Market;
import com.back.takeeat.dto.mainPage.response.MarketInfoResponse;
import com.back.takeeat.repository.MarketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarketListService {

    private final MarketRepository marketRepository;

    @Transactional(readOnly = true)
    public List<MarketInfoResponse> getMarketInfo(Market market) {
        double currentLat = market.getLatitude();
        double currentLon = market.getLongitude();

        // 반경 1.5km
        double distance = 1.5;

        // 1km당 위도와 경도의 변화량
        double deltaLat = distance / 111.0;
        double deltaLon = distance / (111.0 * Math.cos(Math.toRadians(currentLat)));

        // 위도와 경도의 범위
        double minLat = currentLat - deltaLat;
        double maxLat = currentLat + deltaLat;
        double minLon = currentLon - deltaLon;
        double maxLon = currentLon + deltaLon;

        List<Market> findMarkets = marketRepository.findAll();



        return MarketInfoResponse.listOf(findMarkets, minLat, maxLat, minLon, maxLon);
    }
}
