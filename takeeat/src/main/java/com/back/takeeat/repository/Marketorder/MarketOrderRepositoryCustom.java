package com.back.takeeat.repository.Marketorder;

import com.back.takeeat.domain.order.Order;
import com.back.takeeat.dto.marketorder.request.MarketOrderSearchRequest;

import java.util.List;

public interface MarketOrderRepositoryCustom {
    List<Order> findAllWithSortOrder(Long marketId, MarketOrderSearchRequest searchRequest);
}
