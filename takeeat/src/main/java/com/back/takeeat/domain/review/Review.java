package com.back.takeeat.domain.review;

import com.back.takeeat.common.domain.BaseTimeEntity;
import com.back.takeeat.domain.market.Market;
import com.back.takeeat.domain.order.Order;
import com.back.takeeat.domain.user.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
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

    @OneToOne(mappedBy = "review")
    private OwnerReview ownerReview;

    @OneToMany(mappedBy = "review")
    private List<ReviewImage> reviewImages;
}
