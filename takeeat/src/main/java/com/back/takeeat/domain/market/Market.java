package com.back.takeeat.domain.market;

import com.back.takeeat.domain.review.Review;
import com.back.takeeat.domain.user.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Market {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "market_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String marketName;

    private String marketImage;

    private String query;

    private String addressDetail;

    private Double latitude;

    private Double longitude;

    private String marketNumber;

    @Column(length = 3000)
    private String marketIntroduction;

    private String marketCategory;

    private String operationTime;

    private String businessNumber;

    @Enumerated(EnumType.STRING)
    private MarketStatus marketStatus;

    private Double marketRating;

    private int reviewCount;

    private String closedDays;

    public void writeReview(int reviewRating) {
        //평점 계산
        double newRating = ((this.marketRating * this.reviewCount) + reviewRating) / (this.reviewCount + 1);
        this.marketRating = Math.round(newRating * 10) / 10.0;

        //리뷰 수 증가
        this.reviewCount++;
    }
}
