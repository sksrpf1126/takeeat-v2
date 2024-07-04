package com.back.takeeat.dto.notification.request;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestOrderRequest {

    /**
     * 주문 데이터를 생성하기 위한 테스트 DTO
     * 유효성 검증 로직 X
     * memberId의 경우 로그인 로직에 따라 없어질 수도 있다.
     *
     */

    private Long memberId;
    private String requirement;
    private int totalPrice;
    private List<TestOrderMenuRequest> orderMenuRequests;


}
