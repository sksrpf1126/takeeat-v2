package com.back.takeeat.dto.review.response;

import com.back.takeeat.dto.marketMenu.response.MarketMenuResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingCountResponse {

    private long oneStar;
    private long twoStar;
    private long threeStar;
    private long fourStar;
    private long fiveStar;

    public static RatingCountResponse createByMarketRatingResponse(List<MarketRatingResponse> marketRatingResponses) {
        RatingCountResponseBuilder builder = RatingCountResponse.builder();

        for (MarketRatingResponse marketRatingResponse : marketRatingResponses) {
            switch (marketRatingResponse.getReviewRating()) {
                case 1:
                    builder.oneStar(marketRatingResponse.getCount()); break;
                case 2:
                    builder.twoStar(marketRatingResponse.getCount()); break;
                case 3:
                    builder.threeStar(marketRatingResponse.getCount()); break;
                case 4:
                    builder.fourStar(marketRatingResponse.getCount()); break;
                case 5:
                    builder.fiveStar(marketRatingResponse.getCount()); break;
            }
        }

        return builder.build();
    }
}
