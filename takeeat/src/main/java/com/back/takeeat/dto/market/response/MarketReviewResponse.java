package com.back.takeeat.dto.market.response;

import com.back.takeeat.dto.review.response.RatingCountResponse;
import com.back.takeeat.dto.review.response.ReviewResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MarketReviewResponse {

    private double marketRating;

    private RatingCountResponse ratingCountResponse;

    private List<ReviewResponse> allOptionReviews;
    private List<ReviewResponse> noAnswerOptionReviews;
    private List<ReviewResponse> blindOptionReviews;

    public static MarketReviewResponse create(double marketRating, RatingCountResponse ratingCountResponse, List<ReviewResponse> allOptionReviews,
                                              List<ReviewResponse> noAnswerOptionReviews, List<ReviewResponse> blindOptionReviews) {
        return MarketReviewResponse.builder()
                .marketRating(marketRating)
                .ratingCountResponse(ratingCountResponse)
                .allOptionReviews(allOptionReviews)
                .noAnswerOptionReviews(noAnswerOptionReviews)
                .blindOptionReviews(blindOptionReviews)
                .build();
    }
}
