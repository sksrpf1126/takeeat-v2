package com.back.takeeat.service;

import com.back.takeeat.common.exception.AuthException;
import com.back.takeeat.common.exception.EntityNotFoundException;
import com.back.takeeat.common.exception.ErrorCode;
import com.back.takeeat.domain.option.OptionCategory;
import com.back.takeeat.domain.order.Order;
import com.back.takeeat.domain.order.OrderMenu;
import com.back.takeeat.domain.order.OrderStatus;
import com.back.takeeat.dto.marketorder.request.MarketOrderSearchRequest;
import com.back.takeeat.dto.marketorder.response.DetailMarketOrderResponse;
import com.back.takeeat.dto.marketorder.response.MarketOrdersResponse;
import com.back.takeeat.dto.marketorder.response.OrdersCountResponse;
import com.back.takeeat.dto.marketorder.response.optioncategory.DetailMarketOrderResponseV2;
import com.back.takeeat.dto.marketorder.response.optioncategory.MarketOrderMenuResponseV2;
import com.back.takeeat.repository.MarketOrderRepository;
import com.back.takeeat.repository.OrderOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MarketOrderService {

    private final MarketOrderRepository marketOrderRepository;
    private final OrderOptionRepository orderOptionRepository;

    @Transactional(readOnly = true)
    public Map<OrderStatus, Long> findMarketOrderStatus(Long marketId) {

        List<OrdersCountResponse> ordersCountResponses =  marketOrderRepository.findOrdersCounting(marketId);

        Map<OrderStatus, Long> orderStatusLongMap = new EnumMap<>(OrderStatus.class);
        for (OrderStatus status : OrderStatus.values()) {
            orderStatusLongMap.put(status, 0L);
        }

        orderStatusLongMap.putAll(
                ordersCountResponses.stream()
                        .collect(Collectors.toMap(
                                OrdersCountResponse::getOrderStatus,
                                OrdersCountResponse::getOrderCount
                        ))
        );

        return orderStatusLongMap;
    }


    @Transactional(readOnly = true)
    public List<MarketOrdersResponse> getOrdersByStatusCondition(Long marketId, MarketOrderSearchRequest searchRequest) {

        List<Order> findOrders = marketOrderRepository.findAllWithSortOrder(marketId, searchRequest);

        return MarketOrdersResponse.listOf(findOrders);
    }

    @Transactional(readOnly = true)
    public List<MarketOrdersResponse> findAllOrdersWithSearch(Long marketId, MarketOrderSearchRequest searchRequest) {

        List<Order> findOrders = marketOrderRepository.findAllWithSearch(marketId, searchRequest);

        return MarketOrdersResponse.listOf(findOrders);
    }

    @Transactional
    public DetailMarketOrderResponse findDetailMarketOrder(Long orderId) {
        Order findOrderWithMenus = marketOrderRepository.findWithOrderMenus(orderId)
                                        .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ORDER_NOT_FOUND));

        findOrderWithMenus.orderCheck();

        return DetailMarketOrderResponse.of(findOrderWithMenus);
    }

    @Transactional(readOnly = true)
    public DetailMarketOrderResponseV2 findDetailMarketOrderWithOption(Long orderId) {
        Order findOrderWithMenus = marketOrderRepository.findWithOrderMenus(orderId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ORDER_NOT_FOUND));

        List<MarketOrderMenuResponseV2> marketOrderMenuResponseTwos = findOrderWithMenus.getOrderMenus().stream()
                .map(this::makeMarketOrderMenuWithCategory)
                .collect(Collectors.toList());

        return DetailMarketOrderResponseV2.of(findOrderWithMenus, marketOrderMenuResponseTwos);
    }

    private MarketOrderMenuResponseV2 makeMarketOrderMenuWithCategory(OrderMenu orderMenu) {
        List<OptionCategory> optionCategories = orderOptionRepository.findOrderOptionTest(orderMenu.getId());

        return MarketOrderMenuResponseV2.of(orderMenu, optionCategories);
    }

    @Transactional
    public void updateOrderStatusWithTime(Long memberId, Long orderId, OrderStatus currentOrderStatus, OrderStatus targetOrderStatus) {
        Order findOrder = marketOrderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ORDER_NOT_FOUND));

        validateOrderOwner(findOrder, memberId);
        validateOrderStatus(findOrder, currentOrderStatus);

        findOrder.updateOrderStatus(targetOrderStatus);
        findOrder.updateAcceptOrCompleteTime(targetOrderStatus);
    }

    /**
     * 주문을 조회하는 회원이 본인의 주문인지에 대하여 검증한다. (타인의 주문 데이터 조회 방지)
     */
    private void validateOrderOwner(Order order, Long memberId) {
        //@TODO 관리자일 경우에 대해 로직을 추가하거나 다른 메서드로 분리할 것
        if(!memberId.equals(order.getMember().getId())) {
            throw new AuthException(ErrorCode.ORDER_UNAUTHORIZED);
        }
    }

    private void validateOrderStatus(Order order, OrderStatus currentOrderStatus) {
        if(!order.getOrderStatus().equals(currentOrderStatus)) {
            throw new AuthException(ErrorCode.ORDER_STATUS_MISMATCH);
        }
    }

}
