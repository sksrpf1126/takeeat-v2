package com.back.takeeat.dto.myPage.response;

import com.back.takeeat.domain.review.Review;
import com.back.takeeat.domain.review.ReviewImage;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class ReviewModifyFormResponse {

    private String marketName;

    private int reviewRating;
    private String content;

    private List<String> reviewImages;

    public static ReviewModifyFormResponse createByReview(Review review) {
        return ReviewModifyFormResponse.builder()
                .marketName(review.getMarket().getMarketName())
                .reviewRating(review.getReviewRating())
                .content(review.getContent())
                .reviewImages(createReviewImages(review.getReviewImages()))
                .build();
    }

    private static List<String> createReviewImages(List<ReviewImage> reviewImagesObj) {
        List<String> storeNames = new ArrayList<>();
        for (ReviewImage reviewImage : reviewImagesObj) {
            storeNames.add(reviewImage.getStoreName());
        }
        return storeNames;
    }
}
