package com.back.takeeat.domain.review;

import com.back.takeeat.common.domain.BaseTimeEntity;
import com.back.takeeat.domain.market.Market;
import com.back.takeeat.domain.order.Order;
import com.back.takeeat.domain.user.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OwnerReview extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "owner_review_id")
    private Long id;

    private String content;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @Builder
    public OwnerReview(String content, Review review) {
        this.content = content;
        this.review = review;
    }

    public void modify(String content, Review review) {
        this.content = content;
        review.updateOwnerReview(this);
    }

}
