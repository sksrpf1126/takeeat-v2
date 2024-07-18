package com.back.takeeat.repository;

import com.back.takeeat.domain.review.Review;
import com.back.takeeat.dto.review.response.MarketRatingResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query(
            "SELECT r " +
            "FROM Review r INNER JOIN FETCH r.member " +
            "OUTER JOIN FETCH r.ownerReview " +
            "OUTER JOIN FETCH r.reviewImages " +
            "WHERE r.market.id = :marketId " +
            "AND (r.reviewStatus = ReviewStatus.ACTIVE OR r.reviewStatus = ReviewStatus.MODIFY OR r.reviewStatus = ReviewStatus.REPORT) " +
            "ORDER BY r.id DESC"
    )
    List<Review> findByMarketIdForReviewList(@Param("marketId") Long marketId);

    @Query(
            "SELECT new com.back.takeeat.dto.review.response.MarketRatingResponse(r.reviewRating, COUNT(r)) " +
            "FROM Review r " +
            "WHERE r.market.id = :marketId " +
            "AND (r.reviewStatus = ReviewStatus.ACTIVE OR r.reviewStatus = ReviewStatus.MODIFY OR r.reviewStatus = ReviewStatus.REPORT) " +
            "GROUP BY r.reviewRating"
    )
    List<MarketRatingResponse> findRatingCountByMarketId(@Param("marketId") Long marketId);

    @Query(
            "SELECT r " +
            "FROM Review r INNER JOIN FETCH r.market " +
            "OUTER JOIN FETCH r.ownerReview " +
            "OUTER JOIN FETCH r.reviewImages " +
            "WHERE r.member.id = :memberId " +
            "AND (r.reviewStatus = ReviewStatus.ACTIVE OR r.reviewStatus = ReviewStatus.MODIFY OR r.reviewStatus = ReviewStatus.REPORT) " +
            "ORDER BY r.id DESC"
    )
    List<Review> findByMemberIdForReviewList(@Param("memberId") Long memberId);

    @Query(
            "SELECT SUM(r.reviewRating) " +
            "FROM Review r " +
            "WHERE r.market.id = :marketId " +
            "AND (r.reviewStatus = ReviewStatus.ACTIVE OR r.reviewStatus = ReviewStatus.MODIFY OR r.reviewStatus = ReviewStatus.REPORT) "
    )
    Integer getTotalReviewRating(@Param("marketId") Long marketId);

    @Query(
            "SELECT r " +
                    "FROM Review r INNER JOIN FETCH r.member " +
                    "OUTER JOIN FETCH r.ownerReview " +
                    "OUTER JOIN FETCH r.reviewImages " +
                    "WHERE r.market.id = :marketId " +
                    "AND r.reviewStatus = ReviewStatus.BLIND " +
                    "ORDER BY r.id DESC"
    )
    List<Review> findByMarketIdWithBlindStatus(@Param("marketId") Long marketId);
}
