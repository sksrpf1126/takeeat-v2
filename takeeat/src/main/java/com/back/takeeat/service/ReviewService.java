package com.back.takeeat.service;

import com.back.takeeat.common.exception.EntityNotFoundException;
import com.back.takeeat.common.exception.ErrorCode;
import com.back.takeeat.common.exception.ErrorPageException;
import com.back.takeeat.domain.order.Order;
import com.back.takeeat.domain.review.OwnerReview;
import com.back.takeeat.domain.review.Review;
import com.back.takeeat.domain.review.ReviewImage;
import com.back.takeeat.dto.myPage.request.ReviewModifyFormRequest;
import com.back.takeeat.dto.myPage.response.ReviewListResponse;
import com.back.takeeat.dto.myPage.response.ReviewModifyFormResponse;
import com.back.takeeat.repository.OrderRepository;
import com.back.takeeat.repository.OwnerReviewRepository;
import com.back.takeeat.repository.ReviewImageRepository;
import com.back.takeeat.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final OwnerReviewRepository ownerReviewRepository;

    public void write(Long orderId, int reviewRating, String content, List<String> imgUrls) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ORDER_NOT_FOUND));

        Review review = Review.builder()
                .reviewRating(reviewRating)
                .content(content)
                .member(order.getMember())
                .market(order.getMarket())
                .order(order)
                .build();

        for (String imgUrl : imgUrls) {
            ReviewImage reviewImage = new ReviewImage(imgUrl, review);
            reviewImage.associateReview(review);
        }

        reviewRepository.save(review);

        int totalReviewRating = reviewRepository.getTotalReviewRating(order.getMarket().getId());

        //Market의 평점 계산과 리뷰 수 증가
        order.getMarket().writeReview(totalReviewRating);
    }

    @Transactional(readOnly = true)
    public List<ReviewListResponse> getReviewList(Long memberId) {

        List<Review> reviews = reviewRepository.findByMemberIdForReviewList(memberId);

        List<ReviewListResponse> reviewListResponses = reviews.stream()
                .map(ReviewListResponse::createByReview)
                .collect(Collectors.toList());

        return reviewListResponses;
    }

    @Transactional(readOnly = true)
    public ReviewModifyFormResponse getModifyForm(Long memberId, Long reviewId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.REVIEW_NOT_FOUND));

        //리뷰의 작성자가 아니라면
        if (!review.getMember().getId().equals(memberId)) {
            throw new ErrorPageException(ErrorCode.REVIEW_UNAUTHORIZED);
        }

        return ReviewModifyFormResponse.createByReview(review);
    }

    @Transactional(readOnly = true)
    public List<String> getReviewImages(Long reviewId) {
        List<ReviewImage> reviewImages = reviewImageRepository.findByReviewId(reviewId);

        return reviewImages.stream()
                .map(ReviewImage::getStoreName)
                .collect(Collectors.toList());
    }

    public void deleteReviewImages(Long reviewId) {
        reviewImageRepository.deleteByReviewId(reviewId);
    }

    public void modify(ReviewModifyFormRequest reviewModifyFormRequest, List<String> imgUrls) {

        Review review = reviewRepository.findById(reviewModifyFormRequest.getReviewId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.REVIEW_NOT_FOUND));

        int beforeReviewRating = review.getReviewRating();

        review.modify(reviewModifyFormRequest);

        for (String imgUrl : imgUrls) {
            ReviewImage reviewImage = new ReviewImage(imgUrl, review);
            reviewImage.associateReview(review);
        }

        //리뷰 평점이 변경되었다면 Market의 평점 update
        if (beforeReviewRating != reviewModifyFormRequest.getRating()) {
            int totalReviewRating = reviewRepository.getTotalReviewRating(review.getMarket().getId());
            review.getMarket().modifyReview(totalReviewRating);
        }
    }

    public void delete(Long reviewId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.REVIEW_NOT_FOUND));

        review.delete();

        int totalReviewRating = reviewRepository.getTotalReviewRating(review.getMarket().getId());
        review.getMarket().deleteReview(totalReviewRating);
    }

    public String writeOwnerReview(Long reviewId, String ownerReviewContent) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.REVIEW_NOT_FOUND));

        OwnerReview findOwnerReview = review.getOwnerReview();

        /*
        1. 기존에 작성된 답글이 있다
            1-1. 내용이 있다면 수정
            1-2. 내용이 없다면 삭제
        2. 기존에 작성된 답글이 없다
            2-1. 내용이 있다면 작성
            2-2. 내용이 없다면 아무 작업도 하지 않는다
        */
        if (findOwnerReview != null) {
            if (ownerReviewContent != null && !ownerReviewContent.isBlank()) {
                findOwnerReview.modify(ownerReviewContent, review);
                return "modify";
            } else {
                ownerReviewRepository.deleteById(findOwnerReview.getId());
                return "delete";
            }
        } else {
            if (ownerReviewContent != null && !ownerReviewContent.isBlank()) {
                OwnerReview ownerReview = new OwnerReview(ownerReviewContent, review);
                ownerReviewRepository.save(ownerReview);
                return "write";
            } else {
                return "none";
            }
        }
    }

    public void reportReview(Long reviewId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.REVIEW_NOT_FOUND));

        review.report();
    }
}
