package com.back.takeeat.service;

import com.back.takeeat.common.exception.EntityNotFoundException;
import com.back.takeeat.common.exception.ErrorCode;
import com.back.takeeat.domain.market.Market;
import com.back.takeeat.domain.menu.Menu;
import com.back.takeeat.domain.menu.MenuCategory;
import com.back.takeeat.domain.review.OwnerReview;
import com.back.takeeat.domain.review.Review;
import com.back.takeeat.dto.market.request.MarketInfoRequest;
import com.back.takeeat.dto.market.request.MarketMenuCategoryRequest;
import com.back.takeeat.dto.market.request.MarketMenuRequest;
import com.back.takeeat.dto.market.request.MenuRequest;
import com.back.takeeat.dto.market.response.MarketReviewResponse;
import com.back.takeeat.dto.review.response.MarketRatingResponse;
import com.back.takeeat.dto.review.response.RatingCountResponse;
import com.back.takeeat.dto.review.response.ReviewResponse;
import com.back.takeeat.repository.*;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MarketService {

    private final MarketRepository marketRepository;
    private final MenuCategoryRepository menuCategoryRepository;
    private final ReviewRepository reviewRepository;
    private final OwnerReviewRepository ownerReviewRepository;

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
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void MenuCategoriesRegister(MenuRequest menuRequest) {
        for (MarketMenuCategoryRequest marketMenuCategoryRequest : menuRequest.getCategories()) {
            MenuCategory menuCategory = marketMenuCategoryRequest.toMenuCategory();

            // 디버깅 포인트: 메뉴 카테고리 정보 출력
            System.out.println("메뉴 카테고리 저장: " + menuCategory.getMenuCategoryName());

            for (MarketMenuRequest marketMenuRequest : marketMenuCategoryRequest.getMenus()) {
                Menu menu = marketMenuRequest.toMenu();

                // 디버깅 포인트: 메뉴 정보 출력
                System.out.println("메뉴 추가: " + menu.getMenuName());

                menu.addMenuCategory(menuCategory);
                menuCategory.getMenus().add(menu);
            }

            menuCategoryRepository.save(menuCategory);

            // 디버깅 포인트: 저장된 메뉴 카테고리 확인
            System.out.println("메뉴 카테고리 저장 완료: " + menuCategory.getId());
        }
    }

    @Transactional(readOnly = true)
    public MarketReviewResponse getReviewInfo(Long memberId) {

        Market market = marketRepository.findByMemberId(memberId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MARKET_NOT_FOUND));

        //Review
        List<Review> reviews = reviewRepository.findByMarketIdForReviewList(market.getId());

        //Review -> ReviewResponse(allOptionReviews)
        List<ReviewResponse> reviewResponses = reviews.stream()
                .map(ReviewResponse::createByReview)
                .collect(Collectors.toList());

        //reviewResponses -> noAnswerOptionReviews
        List<ReviewResponse> noAnswerOptionReviews = new ArrayList<>();
        for (ReviewResponse reviewResponse : reviewResponses) {
            if (reviewResponse.getOwnerReviewContent() == null) {
                noAnswerOptionReviews.add(reviewResponse);
            }
        }

        //blindOptionReviews
        List<Review> blindReviews = reviewRepository.findByMarketIdWithBlindStatus(market.getId());
        List<ReviewResponse> blindOptionReviews = blindReviews.stream()
                .map(ReviewResponse::createByReview)
                .collect(Collectors.toList());

        //Rating
        List<MarketRatingResponse> marketRatingResponses = reviewRepository.findRatingCountByMarketId(market.getId());
        RatingCountResponse ratingCountResponse = RatingCountResponse.createByMarketRatingResponse(marketRatingResponses);

        return MarketReviewResponse.create(market.getMarketRating(), ratingCountResponse, reviewResponses, noAnswerOptionReviews, blindOptionReviews);
    }

    @Transactional
    public String saveOwnerReview(Long reviewId, String ownerReviewContent) {

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
}
