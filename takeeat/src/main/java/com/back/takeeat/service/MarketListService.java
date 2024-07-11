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
    public List<MarketInfoResponse> getMarketInfo() {
        List<Market> findMarkets = marketRepository.findAll();
        return MarketInfoResponse.listOf(findMarkets);
    }
}
