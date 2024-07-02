package com.back.takeeat.service;

import com.back.takeeat.domain.market.Market;
import com.back.takeeat.domain.review.Review;
import com.back.takeeat.dto.marketMenu.response.*;
import com.back.takeeat.dto.review.response.MarketRatingResponse;
import com.back.takeeat.dto.review.response.RatingCountResponse;
import com.back.takeeat.dto.review.response.ReviewResponse;
import com.back.takeeat.repository.MarketRepository;
import com.back.takeeat.repository.MenuRepository;
import com.back.takeeat.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MarketMenuService {

    private final MarketRepository marketRepository;
    private final MenuRepository menuRepository;
    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public MarketMenuResponse getMarketMenu(Long marketId) {

        MarketResponse marketResponse = null;
        //Market
        Optional<Market> market = marketRepository.findById(marketId);
        if (market.isPresent()) {
            //Market -> MarketResponse
            marketResponse = MarketResponse.createByMarket(market.get());
        } else {
            throw new NoSuchElementException();
        }

        //MenuResponse(MenuCategory, Menu)
        List<MenuResponse> menuResponses = menuRepository.findMenuByMarketId(marketId);

        //MenuResponse(List -> Map)
        List<Long> menuCategoryIds = new ArrayList<>();
        Map<Long, MenuCategoryResponse> menuCategoryMapById = new HashMap<>();
        Map<Long, List<MenuResponse>> menuMapByMenuCategoryId = new HashMap<>();
        Map<Long, MenuResponse> menuMapById = new HashMap<>();
        for (MenuResponse menu : menuResponses) {
            if (!menuCategoryMapById.containsKey(menu.getMenuCategoryId())) {
                menuCategoryIds.add(menu.getMenuCategoryId());
                menuCategoryMapById.put(menu.getMenuCategoryId(), MenuCategoryResponse.create(menu.getMenuCategoryName()));
                menuMapByMenuCategoryId.put(menu.getMenuCategoryId(), new ArrayList<>(List.of(menu)));
            } else {
                menuMapByMenuCategoryId.get(menu.getMenuCategoryId()).add(menu);
            }
            menuMapById.put(menu.getMenuId(), menu);
        }

        //OptionCategoryResponse
        List<OptionCategoryResponse> optionCategoryResponses = menuRepository.findOptionCategoryByMarketId(marketId);

        //OptionCategoryResponse(List -> Map)
        List<Long> menuIds = new ArrayList<>();
        Map<Long, List<OptionCategoryResponse>> optionCategoryMapByMenuId = new HashMap<>();
        for (OptionCategoryResponse optionCategory : optionCategoryResponses) {
            if (!optionCategoryMapByMenuId.containsKey(optionCategory.getMenuId())) {
                menuIds.add(optionCategory.getMenuId());
                optionCategoryMapByMenuId.put(optionCategory.getMenuId(), new ArrayList<>(List.of(optionCategory)));
            } else {
                optionCategoryMapByMenuId.get(optionCategory.getMenuId()).add(optionCategory);
            }
        }

        //OptionResponse
        List<OptionResponse> OptionResponses = menuRepository.findOptionByMarketId(marketId);

        //OptionResponse(List -> Map)
        Map<Long, List<OptionResponse>> optionMapByOptionCategoryId = new HashMap<>();
        for (OptionResponse option : OptionResponses) {
            if (!optionMapByOptionCategoryId.containsKey(option.getOptionCategoryId())) {
                optionMapByOptionCategoryId.put(option.getOptionCategoryId(), new ArrayList<>(List.of(option)));
            } else {
                optionMapByOptionCategoryId.get(option.getOptionCategoryId()).add(option);
            }
        }

        //Review
        List<Review> reviews = reviewRepository.findByMarketId(marketId);
        //Review -> ReviewResponse
        List<ReviewResponse> reviewResponses = new ArrayList<>();
        for (Review review : reviews) {
            reviewResponses.add(ReviewResponse.createByReview(review));
        }

        //Rating
        List<MarketRatingResponse> marketRatingResponses = reviewRepository.findRatingCountByMarketId(marketId);
        RatingCountResponse ratingCountResponse = RatingCountResponse.createByMarketRatingResponse(marketRatingResponses);

        return MarketMenuResponse.create(marketResponse, menuCategoryIds, menuCategoryMapById, menuMapByMenuCategoryId,
                menuIds, menuMapById, optionCategoryMapByMenuId, optionMapByOptionCategoryId, reviewResponses, ratingCountResponse);
    }
}
