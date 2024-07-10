package com.back.takeeat.repository;

import com.back.takeeat.domain.review.OwnerReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerReviewRepository extends JpaRepository<OwnerReview, Long> {
}
