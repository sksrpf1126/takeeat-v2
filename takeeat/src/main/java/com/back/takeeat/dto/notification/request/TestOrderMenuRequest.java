package com.back.takeeat.dto.notification.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestOrderMenuRequest {

    private Long menuId;
    private int orderQuantity;
    List<Long> optionIds;

}
