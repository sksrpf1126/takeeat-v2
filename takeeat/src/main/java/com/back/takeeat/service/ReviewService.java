package com.back.takeeat.service;

import com.back.takeeat.domain.order.Order;
import com.back.takeeat.domain.review.Review;
import com.back.takeeat.domain.review.ReviewImage;
import com.back.takeeat.repository.OrderRepository;
import com.back.takeeat.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;

    public void write(Long orderId, int rating, String content, List<String> imgUrls) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(NoSuchElementException::new);

        Review review = new Review(rating, content, order.getMember(), order.getMarket(), order);

        for (String imgUrl : imgUrls) {
            ReviewImage reviewImage = new ReviewImage(imgUrl, review);
            reviewImage.associateReview(review);
        }

        reviewRepository.save(review);

        //Market의 평점 계산과 리뷰 수 증가
        order.getMarket().writeReview(rating);
    }
}
