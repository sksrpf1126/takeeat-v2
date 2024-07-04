package com.back.takeeat.service;

import com.back.takeeat.domain.market.Market;
import com.back.takeeat.domain.menu.Menu;
import com.back.takeeat.domain.menu.MenuCategory;
import com.back.takeeat.dto.market.request.MarketInfoRequest;
import com.back.takeeat.dto.market.request.MarketMenuCategoryRequest;
import com.back.takeeat.dto.market.request.MarketMenuRequest;
import com.back.takeeat.dto.market.response.MarketMenuCategoryResponse;
import com.back.takeeat.repository.MarketMenuRepository;
import com.back.takeeat.repository.MarketRepository;
import com.back.takeeat.repository.MenuCategoryRepository;
import com.back.takeeat.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MarketService {

    private final MarketRepository marketRepository;
    private final MenuCategoryRepository menuCategoryRepository;
    private final MarketMenuRepository marketMenuRepository;

    @Transactional(readOnly = false)
    public void marketInfoRegister(MarketInfoRequest marketInfoRequest) {
        Market market = marketInfoRequest.toMarket();
        marketRepository.save(market);
    }

    @Transactional(readOnly = true)
    public boolean checkMarketNameDuplicate(String marketName) {
        return marketRepository.existsByMarketName(marketName);
    }

}
