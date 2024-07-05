package com.back.takeeat.dto.marketMenu.response;

import com.back.takeeat.dto.review.response.RatingCountResponse;
import com.back.takeeat.dto.review.response.ReviewResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketMenuResponse {

    private MarketResponse marketResponse;

    private List<Long> menuCategoryIds;
    private Map<Long, MenuCategoryResponse> menuCategoryMapById;
    private Map<Long, List<MenuResponse>> menuMapByMenuCategoryId;

    private List<Long> menuIds;
    private Map<Long, MenuResponse> menuMapById;
    private Map<Long, List<OptionCategoryResponse>> optionCategoryMapByMenuId;
    private Map<Long, List<OptionResponse>> optionMapByOptionCategoryId;

    private List<ReviewResponse> reviewResponses;
    private RatingCountResponse ratingCountResponse;

    public static MarketMenuResponse create(MarketResponse marketResponse, List<Long> menuCategoryIds, Map<Long, MenuCategoryResponse> menuCategoryMapById,
                                            Map<Long, List<MenuResponse>> menuMapByMenuCategoryId, List<Long> menuIds, Map<Long, MenuResponse> menuMapById,
                                            Map<Long, List<OptionCategoryResponse>> optionCategoryMapByMenuId, Map<Long, List<OptionResponse>> optionMapByOptionCategoryId,
                                            List<ReviewResponse> reviewResponses, RatingCountResponse ratingCountResponse) {
        return MarketMenuResponse.builder()
                .marketResponse(marketResponse)
                .menuCategoryIds(menuCategoryIds)
                .menuCategoryMapById(menuCategoryMapById)
                .menuMapByMenuCategoryId(menuMapByMenuCategoryId)
                .menuIds(menuIds)
                .menuMapById(menuMapById)
                .optionCategoryMapByMenuId(optionCategoryMapByMenuId)
                .optionMapByOptionCategoryId(optionMapByOptionCategoryId)
                .reviewResponses(reviewResponses)
                .ratingCountResponse(ratingCountResponse)
                .build();
    }
}
