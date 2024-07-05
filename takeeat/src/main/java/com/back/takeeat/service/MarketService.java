package com.back.takeeat.service;

import com.back.takeeat.domain.market.Market;
import com.back.takeeat.domain.menu.Menu;
import com.back.takeeat.domain.menu.MenuCategory;
import com.back.takeeat.dto.market.request.MarketInfoRequest;
import com.back.takeeat.dto.market.request.MarketMenuCategoryRequest;
import com.back.takeeat.dto.market.request.MarketMenuRequest;
import com.back.takeeat.dto.market.request.MenuRequest;
import com.back.takeeat.repository.MarketRepository;
import com.back.takeeat.repository.MenuCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MarketService {

    private final MarketRepository marketRepository;
    private final MenuCategoryRepository menuCategoryRepository;

    @Transactional(readOnly = false)
    public void marketInfoRegister(MarketInfoRequest marketInfoRequest) {
        Market market = marketInfoRequest.toMarket();
        marketRepository.save(market);
    }

    @Transactional(readOnly = true)
    public boolean checkMarketNameDuplicate(String marketName) {
        return marketRepository.existsByMarketName(marketName);
    }

    @Transactional
    public void MenuCategoriesRegister(MenuRequest menuRequest) {
        for (MarketMenuCategoryRequest marketMenuCategoryRequest : menuRequest.getCategories()) {
            MenuCategory menuCategory = marketMenuCategoryRequest.toMenuCategory();
            for (MarketMenuRequest marketMenuRequest : marketMenuCategoryRequest.getMenus()) {
                Menu menu = marketMenuRequest.toMenu();
                menuCategory.getMenus().add(menu);
            }

            menuCategoryRepository.save(menuCategory);
        }
    }

}
