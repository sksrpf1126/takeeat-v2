package com.back.takeeat.domain.market;

import com.back.takeeat.domain.user.Member;
import jakarta.persistence.*;

@Entity
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

    @Enumerated(EnumType.STRING)
    private MarketStatus marketStatus;

    private Double marketRating;

    private int reviewCount;

}
