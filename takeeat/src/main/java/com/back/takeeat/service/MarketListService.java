package com.back.takeeat.service;

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
    public List<MarketInfoResponse> getMarketInfo(String marketCategory
                                                , double latitude, double longitude) {
        // 반경 4.0km
        double distance = 4.0;

        // 1km당 위도와 경도의 변화량
        double deltaLat = distance / 111.0;
        double deltaLon = distance / (111.0 * Math.cos(Math.toRadians(latitude)));

        // 위도와 경도의 범위
        double minLat = latitude - deltaLat;
        double maxLat = latitude + deltaLat;
        double minLon = longitude - deltaLon;
        double maxLon = longitude + deltaLon;

        System.out.println("minLat"+minLat);
        System.out.println("maxLat"+maxLat);
        System.out.println("minLon"+minLon);
        System.out.println("maxLon"+maxLon);

        List<MarketInfoResponse> findMarkets = marketRepository.findMarketByLatLon(marketCategory, minLat, maxLat, minLon, maxLon, latitude, longitude);
        System.out.println("listsize:"+findMarkets.size());
        return findMarkets;
    }
}
