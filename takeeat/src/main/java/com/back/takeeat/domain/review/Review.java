package com.back.takeeat.domain.review;

import com.back.takeeat.common.domain.BaseTimeEntity;
import com.back.takeeat.domain.market.Market;
import com.back.takeeat.domain.order.Order;
import com.back.takeeat.domain.order.OrderMenu;
import com.back.takeeat.domain.user.Member;
import com.back.takeeat.dto.myPage.request.ReviewModifyFormRequest;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    private int reviewRating;

    private String content;

    @Enumerated(EnumType.STRING)
    private ReviewStatus reviewStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "market_id")
    private Market market;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToOne(mappedBy = "review", fetch = FetchType.LAZY)
    private OwnerReview ownerReview;

    @OneToMany(mappedBy = "review", cascade = CascadeType.PERSIST)
    private List<ReviewImage> reviewImages = new ArrayList<>();

    @Builder
    public Review(int reviewRating, String content, Member member, Market market, Order order) {
        this.reviewRating = reviewRating;
        this.content = content;
        this.member = member;
        this.market = market;
        this.order = order;
        this.reviewStatus = ReviewStatus.ACTIVE;
    }

    public void deleteReviewImage(ReviewImage reviewImage) {
        this.reviewImages.remove(reviewImage);
    }

    public void addReviewImage(ReviewImage reviewImage) {
        this.reviewImages.add(reviewImage);
    }

    public void modify(ReviewModifyFormRequest modifyForm) {
        this.reviewRating = modifyForm.getRating();
        this.content = modifyForm.getContent();
        this.reviewStatus = ReviewStatus.MODIFY;
    }

    public void delete() {
        this.reviewStatus = ReviewStatus.DELETE;
    }
}
