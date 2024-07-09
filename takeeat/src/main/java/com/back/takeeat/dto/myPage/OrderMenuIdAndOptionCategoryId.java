package com.back.takeeat.dto.myPage;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class OrderMenuIdAndOptionCategoryId {

    private Long orderMenuId;
    private Long optionCategoryId;
}
