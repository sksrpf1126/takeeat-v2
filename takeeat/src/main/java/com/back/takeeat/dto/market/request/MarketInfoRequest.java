package com.back.takeeat.dto.market.request;

import com.back.takeeat.domain.market.Market;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MarketInfoRequest {
    /*@NotEmpty(message = "가게 이름은 필수 입니다")*/
    private String marketName; // 가게 이름

    /*@NotEmpty(message = "가게 사진은 필수 입니다")*/
    private String marketImage; // 가게 사진

    /*@NotEmpty(message = "가게 주소는 필수 입니다")*/
    private String query; // 도로명 주소

    private String addressDetail; // 상세 주소

    private String businessNumber; // 사업자 번호

    /*@NotEmpty(message = "가게 전화번호는 필수 입니다")*/
    private String marketNumber; // 가게 전화번호

    /*@NotEmpty(message = "가게 카테고리는 필수 입니다")*/
    private String marketCategory; // 가게 카테고리

    private String marketIntroduction; // 가게 소개글

    private String openTime; // 운영시간

    private String closeTime;

    private String operationTime;

    private String closedDays; // 휴무일

    public String operationTime(){
        return operationTime = openTime + " ~ " + closeTime;
    }

    public MarketInfoRequest marketInfoRequest() {
        return MarketInfoRequest.builder()
                .marketName(marketName)
                .marketImage(marketImage)
                .query(query)
                .addressDetail(addressDetail)
                .businessNumber(businessNumber)
                .marketNumber(marketNumber)
                .marketCategory(marketCategory)
                .marketIntroduction(marketIntroduction)
                .operationTime(operationTime())
                .closedDays(closedDays)
                .build();
    }

    public Market toMarket() {
        return Market.builder()
                .marketName(marketName)
                .marketImage(marketImage)
                .query(query)
                .addressDetail(addressDetail)
                .businessNumber(businessNumber)
                .marketNumber(marketNumber)
                .marketCategory(marketCategory)
                .marketIntroduction(marketIntroduction)
                .operationTime(operationTime())
                .closedDays(closedDays)
                .build();
    }
}