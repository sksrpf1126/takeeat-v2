package com.back.takeeat.dto.marketorder.request;

import com.back.takeeat.domain.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MarketOrderSearchRequest {

    private OrderStatus orderStatus;
    private String sortOrder;

}
