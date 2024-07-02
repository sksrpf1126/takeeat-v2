package com.back.takeeat.dto.review.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MarketRatingResponse {

    private Integer reviewRating;
    private Long count;
}
