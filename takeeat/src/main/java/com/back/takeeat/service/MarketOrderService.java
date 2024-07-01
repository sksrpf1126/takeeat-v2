package com.back.takeeat.service;

import com.back.takeeat.domain.order.Order;
import com.back.takeeat.domain.order.OrderStatus;
import com.back.takeeat.dto.marketorder.request.MarketOrderSearchRequest;
import com.back.takeeat.dto.marketorder.response.MarketOrdersResponse;
import com.back.takeeat.dto.marketorder.response.OrdersCountResponse;
import com.back.takeeat.repository.MarketOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MarketOrderService {

    private final MarketOrderRepository marketOrderRepository;

    @Transactional(readOnly = true)
    public Map<OrderStatus, Long> findMarketOrderStatus(Long marketId) {

        List<OrdersCountResponse> ordersCountResponses =  marketOrderRepository.findOrdersCounting(marketId);

        Map<OrderStatus, Long> orderStatusLongMap = ordersCountResponses.stream()
                .collect(Collectors.toMap((OrdersCountResponse::getOrderStatus), (OrdersCountResponse::getOrderCount)));

        return orderStatusLongMap;
    }


    @Transactional(readOnly = true)
    public List<MarketOrdersResponse> getOrdersByStatusCondition(Long marketId, MarketOrderSearchRequest searchRequest) {

        List<Order> findOrders = marketOrderRepository.findAllWithSortOrder(marketId, searchRequest);

        return MarketOrdersResponse.listOf(findOrders);
    }

}
