package com.back.takeeat.dto.marketorder.request;

import com.back.takeeat.domain.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Getter
@AllArgsConstructor
public class MarketOrderSearchRequest {

    private List<OrderStatus> orderStatuses;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date selectDate;
    private String sortOrder;

}
