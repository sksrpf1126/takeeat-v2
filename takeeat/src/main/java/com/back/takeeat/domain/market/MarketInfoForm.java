package com.back.takeeat.domain.market;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

@Getter
@Setter
public class MarketInfoForm {
    @NotEmpty(message = "가게 이름은 필수 입니다")
    private String marketName; // 가게 이름

    @NotEmpty(message = "가게 사진은 필수 입니다")
    private String marketImage; // 가게 사진

    @NotEmpty(message = "가게 주소는 필수 입니다")
    private String query; // 도로명 주소

    private String addressDetail; // 상세 주소

    private String businessNumber; // 사업자 번호

    @NotEmpty(message = "가게 전화번호는 필수 입니다")
    private String marketNumber; // 가게 전화번호

    @NotEmpty(message = "가게 카테고리는 필수 입니다")
    private String marketCategory; // 가게 카테고리

    private String marketIntroduction; // 가게 소개글

    private String operationTime; // 운영시간

    private String closedDays; // 휴무일


}