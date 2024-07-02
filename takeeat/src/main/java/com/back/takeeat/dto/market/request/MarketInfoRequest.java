package com.back.takeeat.dto.market.request;

import com.back.takeeat.domain.market.Market;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
@Builder
@AllArgsConstructor
public class MarketInfoRequest {
    @NotEmpty(message = "가게 이름은 필수 입니다")
    @Size(min = 2, max = 20, message = "가게 이름은 2자 이상, 20자 이하로 입력해주세요.")
    private String marketName; // 가게 이름

    @NotEmpty(message = "가게 사진은 필수 입니다")
    private String marketImage; // 가게 사진

    @NotEmpty(message = "가게 주소는 필수 입니다")
    private String query; // 도로명 주소

    private String addressDetail; // 상세 주소

    @NotEmpty(message = "사업자번호는 필수 입니다")
    private String businessNumber; // 사업자 번호

    @NotEmpty(message = "가게 전화번호는 필수 입니다")
    @Length(max=11)
    private String marketNumber; // 가게 전화번호

    @NotEmpty(message = "가게 카테고리는 필수 입니다")
    private String marketCategory; // 가게 카테고리

    private String marketIntroduction; // 가게 소개글

    private String openTime; // 운영시간

    private String closeTime;

    private String operationTime;

    private String closedDays; // 휴무일




    public String hyphenNumber(){
        if (marketNumber.length() == 10) {
            if (marketNumber.startsWith("02")) {
               return  marketNumber.substring(0,2) + "-" + marketNumber.substring(2,6) + "-" + marketNumber.substring(6,10);
            }
            return marketNumber.substring(0,3) + "-" + marketNumber.substring(3,6) + "-" + marketNumber.substring(6,10);
        } else if (marketNumber.length() == 11){
            return marketNumber.substring(0,3) + "-" + marketNumber.substring(3,7) + "-" + marketNumber.substring(7,11);
        }
        return marketNumber;
    }

    // 빌드

    public MarketInfoRequest marketInfoRequest() {
        return MarketInfoRequest.builder()
                .marketName(marketName)
                .marketImage(marketImage)
                .query(query)
                .addressDetail(addressDetail)
                .businessNumber(businessNumber)
                .marketNumber(hyphenNumber())
                .marketCategory(marketCategory)
                .marketIntroduction(marketIntroduction)
                .operationTime(openTime + " ~ " + closeTime)
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
                .marketNumber(hyphenNumber())
                .marketCategory(marketCategory)
                .marketIntroduction(marketIntroduction)
                .operationTime(operationTime)
                .closedDays(closedDays)
                .build();
    }
}