package com.back.takeeat.domain.review;

import com.back.takeeat.domain.menu.Menu;
import com.back.takeeat.domain.order.Order;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_image_id")
    private Long id;

    private String storeName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @Builder
    public ReviewImage(String storeName, Review review) {
        this.storeName = storeName;
        this.review = review;
    }

    public void associateReview(Review review) {
        if (this.review != null) {
            review.deleteReviewImage(this);
        }

        review.addReviewImage(this);
        this.review = review;
    }

}
