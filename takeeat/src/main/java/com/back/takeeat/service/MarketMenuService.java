package com.back.takeeat.service;

import com.back.takeeat.domain.market.Market;
import com.back.takeeat.dto.marketMenu.response.MarketMenuResponse;
import com.back.takeeat.dto.marketMenu.response.MarketResponse;
import com.back.takeeat.dto.marketMenu.response.MenuResponse;
import com.back.takeeat.repository.MarketRepository;
import com.back.takeeat.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MarketMenuService {

    private final MarketRepository marketRepository;
    private final MenuRepository menuRepository;

    @Transactional(readOnly = true)
    public MarketMenuResponse getMarketMenu(Long marketId) {

        MarketResponse marketResponse = null;

        Optional<Market> market = marketRepository.findById(marketId);
        if (market.isPresent()) {
            marketResponse = MarketResponse.createMarketResponse(market.get());
        } else {
            throw new NoSuchElementException();
        }

        List<MenuResponse> menuResponses = menuRepository.findMenusById(marketId);

        return MarketMenuResponse.createMarketMenuResponse(marketResponse, menuResponses);
    }
}
