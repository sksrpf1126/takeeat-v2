package com.back.takeeat.dto.market.response;

import com.back.takeeat.domain.market.Market;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MarketInfoResponse {
    @NotBlank(message = "가게 이름은 필수 입니다.")
    @Size(min = 2, max = 20, message = "가게 이름은 2자 이상, 20자 이하로 입력해주세요.")
    private String marketName; // 가게 이름

    @NotBlank(message = "가게 사진은 필수 입니다.")
    private String marketImage; // 가게 사진

    @NotBlank(message = "가게 주소는 필수 입니다.")
    private String query; // 도로명 주소

    private String addressDetail; // 상세 주소

    @NotBlank(message = "사업자번호는 필수 입니다.")
    private String businessNumber; // 사업자 번호

    @NotBlank(message = "가게 전화번호는 필수 입니다.")
    private String marketNumber; // 가게 전화번호

    @NotBlank(message = "가게 카테고리는 필수 입니다.")
    private String marketCategory; // 가게 카테고리

    private String marketIntroduction; // 가게 소개글

    private String operationTime; // 운영시간

    private String closedDays; // 휴무일

    public static MarketInfoResponse of(Market market) {
        return MarketInfoResponse.builder()
                .marketName(market.getMarketName())
                .marketImage(market.getMarketImage())
                .query(market.getQuery())
                .addressDetail(market.getAddressDetail())
                .businessNumber(market.getBusinessNumber())
                .marketNumber(market.getMarketNumber())
                .marketCategory(market.getMarketCategory())
                .marketIntroduction(market.getMarketIntroduction())
                .operationTime(market.getOperationTime())
                .closedDays(market.getClosedDays())
                .build();
    }

}