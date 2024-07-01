package com.back.takeeat.service;

import com.back.takeeat.domain.market.Market;
import com.back.takeeat.dto.market.request.MarketInfoRequest;
import com.back.takeeat.repository.MarketInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MarketInfoService {

    private final MarketInfoRepository marketInfoRepository;

    @Transactional(readOnly = false)
    public void register(MarketInfoRequest request) {
        Market market = request.toMarket();
        marketInfoRepository.save(market);
    }
}
