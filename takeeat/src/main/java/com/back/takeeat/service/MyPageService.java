package com.back.takeeat.service;

import com.back.takeeat.domain.order.Order;
import com.back.takeeat.dto.myPage.response.OrderListResponse;
import com.back.takeeat.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final OrderRepository orderRepository;

    public List<OrderListResponse> getOrderList(Long memberId) {

        List<Order> orders = orderRepository.findByMemberIdForOrderList(memberId);

        List<OrderListResponse> orderListResponses = orders.stream()
                .map(OrderListResponse::createByOrder)
                .collect(Collectors.toList());

        return orderListResponses;
    }
}
