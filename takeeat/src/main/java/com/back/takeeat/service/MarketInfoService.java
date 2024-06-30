package com.back.takeeat.service;

import com.back.takeeat.domain.market.Market;
import com.back.takeeat.repository.MarketInfoRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MarketInfoService {

    private final MarketInfoRepository marketInfoRepository;

    @Transactional
    public void saveMarket(Market market) {
        marketInfoRepository.saveMarket(market);
    }



}
