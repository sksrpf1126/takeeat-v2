package com.back.takeeat.service;

import com.back.takeeat.common.exception.EntityNotFoundException;
import com.back.takeeat.common.exception.ErrorCode;
import com.back.takeeat.domain.market.Market;
import com.back.takeeat.domain.review.Review;
import com.back.takeeat.dto.marketMenu.response.*;
import com.back.takeeat.dto.review.response.MarketRatingResponse;
import com.back.takeeat.dto.review.response.RatingCountResponse;
import com.back.takeeat.dto.review.response.ReviewResponse;
import com.back.takeeat.repository.MarketRepository;
import com.back.takeeat.repository.MenuCategoryRepository;
import com.back.takeeat.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MarketMenuService {

    private final MarketRepository marketRepository;
    private final MenuCategoryRepository menuCategoryRepository;
    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public MarketMenuResponse getMarketMenu(Long marketId) {

        //Market -> MarketResponse
        Market market = marketRepository.findById(marketId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MARKET_NOT_FOUND));
        MarketResponse marketResponse = MarketResponse.createByMarket(market);

        //MenuResponse(MenuCategory, Menu)
        List<MenuResponse> menuResponses = menuCategoryRepository.findMenuByMarketId(marketId);

        //MenuResponse(List -> Map)
        List<Long> menuCategoryIds = new ArrayList<>();
        List<Long> menuIds = new ArrayList<>();
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
            menuIds.add(menu.getMenuId());
            menuMapById.put(menu.getMenuId(), menu);
        }

        //OptionCategoryResponse
        List<OptionCategoryResponse> optionCategoryResponses = menuCategoryRepository.findOptionCategoryByMarketId(marketId);

        //OptionCategoryResponse(List -> Map)
        Map<Long, List<OptionCategoryResponse>> optionCategoryMapByMenuId = optionCategoryResponses.stream()
                .collect(Collectors.groupingBy(OptionCategoryResponse::getMenuId));

        //OptionResponse
        List<OptionResponse> optionResponses = menuCategoryRepository.findOptionByMarketId(marketId);

        //OptionResponse(List -> Map)
        Map<Long, List<OptionResponse>> optionMapByOptionCategoryId = optionResponses.stream()
                .collect(Collectors.groupingBy(OptionResponse::getOptionCategoryId));

        //Review
        List<Review> reviews = reviewRepository.findByMarketIdForReviewList(marketId);
        //Review -> ReviewResponse
        List<ReviewResponse> reviewResponses = reviews.stream()
                .map(ReviewResponse::createByReview)
                .collect(Collectors.toList());

        //Rating
        List<MarketRatingResponse> marketRatingResponses = reviewRepository.findRatingCountByMarketId(marketId);
        RatingCountResponse ratingCountResponse = RatingCountResponse.createByMarketRatingResponse(marketRatingResponses);

        return MarketMenuResponse.create(marketResponse, menuCategoryIds, menuCategoryMapById, menuMapByMenuCategoryId,
                menuIds, menuMapById, optionCategoryMapByMenuId, optionMapByOptionCategoryId, reviewResponses, ratingCountResponse);
    }
}
