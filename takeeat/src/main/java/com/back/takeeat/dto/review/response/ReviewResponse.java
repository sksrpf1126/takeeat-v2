package com.back.takeeat.dto.review.response;

import com.back.takeeat.domain.review.Review;
import com.back.takeeat.domain.review.ReviewImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {

    String nickname;
    String profile;

    int reviewRating;
    String reviewContent;
    String reviewWriteDate;

    String ownerReviewContent;
    String ownerReviewWriteDate;

    List<String> reviewImages;

    public static ReviewResponse createByReview(Review review) {
        ReviewResponseBuilder builder = ReviewResponse.builder()
                .nickname(review.getMember().getNickname())
                .profile(review.getMember().getProfile())
                .reviewRating(review.getReviewRating())
                .reviewContent(review.getContent())
                .reviewWriteDate(localDateTimeFormat(review.getCreatedTime()))
                .reviewImages(createReviewImages(review.getReviewImages()));

        if (review.getOwnerReview() != null && review.getOwnerReview().getContent() != null) {
           builder.ownerReviewContent(review.getOwnerReview().getContent())
                   .ownerReviewWriteDate(localDateTimeFormat(review.getOwnerReview().getCreatedTime()));
        }

        return builder.build();
    }

    private static List<String> createReviewImages(List<ReviewImage> reviewImagesObj) {
        List<String> storeNames = new ArrayList<>();
        for (ReviewImage reviewImage : reviewImagesObj) {
            storeNames.add(reviewImage.getStoreName());
        }
        return storeNames;
    }

    private static String localDateTimeFormat(LocalDateTime createdTime) {
        LocalDateTime todayDateTime = LocalDateTime.now();
        LocalDate today = todayDateTime.toLocalDate();
        LocalDate yesterday = today.minusDays(1);
        LocalDate writeDate = createdTime.toLocalDate();

        if (writeDate.isEqual(today)) {
            return "오늘";
        } else if (writeDate.isEqual(yesterday)) {
            return "어제";
        } else {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
            return dateTimeFormatter.format(writeDate);
        }

    }
}
