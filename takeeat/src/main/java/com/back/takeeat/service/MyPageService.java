package com.back.takeeat.service;

import com.back.takeeat.common.exception.AccessDeniedException;
import com.back.takeeat.domain.order.Order;
import com.back.takeeat.domain.order.OrderMenu;
import com.back.takeeat.domain.order.OrderStatus;
import com.back.takeeat.dto.myPage.OrderMenuIdAndOptionCategoryId;
import com.back.takeeat.dto.myPage.response.*;
import com.back.takeeat.repository.OrderMenuRepository;
import com.back.takeeat.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MyPageService {

    private final OrderRepository orderRepository;
    private final OrderMenuRepository orderMenuRepository;

    @Transactional(readOnly = true)
    public List<OrderListResponse> getOrderList(Long memberId) {

        List<Order> orders = orderRepository.findByMemberIdForOrderList(memberId);

        List<OrderListResponse> orderListResponses = orders.stream()
                .map(OrderListResponse::createByOrder)
                .collect(Collectors.toList());

        return orderListResponses;
    }

    @Transactional(readOnly = true)
    public OrderDetailResponse getOrderDetail(Long orderId) {

        //주문 정보
        Order order = orderRepository.findById(orderId)
                .orElseThrow(NoSuchElementException::new);

        //주문에 담긴 메뉴 관련 정보
        List<OrderMenuResponse> cartMenuResponses = getOrderMenuResponses(order);

        //주문에 담긴 메뉴별 OptionCategory
        Map<Long, List<OrderOptionCategoryResponse>> optionCategoryByOrderMenu = getOptionCategoryByOrderMenu(order);

        //(OrderMenuId, OptionCategory)별 선택된 Option 이름
        Map<OrderMenuIdAndOptionCategoryId, List<OrderOptionResponse>> orderOptionMapByOptionCategoryId = getOrderOptionMapByOptionCategoryId(order);

        return OrderDetailResponse.create(order, cartMenuResponses, optionCategoryByOrderMenu, orderOptionMapByOptionCategoryId);
    }

    public List<OrderMenuResponse> getOrderMenuResponses(Order order) {
        List<OrderMenuResponse> orderMenuResponses = new ArrayList<>();
        for (OrderMenu orderMenu : order.getOrderMenus()) {
            orderMenuResponses.add(OrderMenuResponse.create(orderMenu.getId(), orderMenu.getOrderQuantity(),
                    orderMenu.getOrderPrice(), orderMenu.getMenu().getMenuName(), orderMenu.getMenu().getMenuPrice()));
        }
        return orderMenuResponses;
    }

    public Map<Long, List<OrderOptionCategoryResponse>> getOptionCategoryByOrderMenu(Order order) {
        List<OrderOptionCategoryResponse> orderOptionCategoryResponses = orderMenuRepository.findByOrderIdWithOptionCategory(order.getId());
        Map<Long, List<OrderOptionCategoryResponse>> optionCategoryByOrderMenu = orderOptionCategoryResponses.stream()
                .collect(Collectors.groupingBy(OrderOptionCategoryResponse::getOrderMenuId));
        return optionCategoryByOrderMenu;
    }

    public Map<OrderMenuIdAndOptionCategoryId, List<OrderOptionResponse>> getOrderOptionMapByOptionCategoryId(Order order) {
        List<OrderOptionResponse> orderOptionResponses = orderMenuRepository.findByOrderIdWithOption(order.getId());
        Map<OrderMenuIdAndOptionCategoryId, List<OrderOptionResponse>> orderOptionMapByOptionCategoryId = orderOptionResponses.stream()
                .collect(Collectors.groupingBy(orderOptionResponse ->
                        new OrderMenuIdAndOptionCategoryId(
                                orderOptionResponse.getOrderMenuId(),
                                orderOptionResponse.getOptionCategoryId()
                        )
                ));
        return orderOptionMapByOptionCategoryId;
    }

    @Transactional(readOnly = true)
    public String getOrderMarketName(Long memberId, Long orderId) throws AccessDeniedException {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(NoSuchElementException::new);

        //주문자가 아니라면, 또는 주문이 완료된 주문건이 아니라면, 또는 이미 리뷰가 존재한다면
        if (!order.getMember().getId().equals(memberId) || order.getOrderStatus() != OrderStatus.COMPLETE || order.getReview() != null) {
            throw new AccessDeniedException();
        }

        return order.getMarket().getMarketName();
    }
}
