package com.back.takeeat.repository;

import com.back.takeeat.domain.review.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {

    List<ReviewImage> findByReviewId(@Param("reviewId") Long reviewId);

    void deleteByReviewId(@Param("reviewId") Long reviewId);
}
