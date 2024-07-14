package com.back.takeeat.domain.market;

import com.back.takeeat.domain.menu.MenuCategory;
import com.back.takeeat.domain.user.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @OneToMany(mappedBy = "market")
    private List<MenuCategory> menuCategories;

    public void writeReview(int totalReviewRating) {
        //리뷰 수 증가
        this.reviewCount++;

        //평점 계산
        double newRating = (double) totalReviewRating / this.reviewCount;
        this.marketRating = Math.round(newRating * 10) / 10.0;
    }

    public void modifyReview(int totalReviewRating) {
        //평점 계산
        double newRating = (double) totalReviewRating / this.reviewCount;
        this.marketRating = Math.round(newRating * 10) / 10.0;
    }

    public void deleteReview(int totalReviewRating) {
        //리뷰 수 감소
        this.reviewCount--;

        //평점 계산
        double newRating = (double) totalReviewRating / this.reviewCount;
        this.marketRating = Math.round(newRating * 10) / 10.0;
    }

    public void addMarketImage(String marketImage) {
        this.marketImage = marketImage;
    }

    public void addMarketStatus(MarketStatus marketStatus) {
        this.marketStatus = marketStatus;
    }
}
