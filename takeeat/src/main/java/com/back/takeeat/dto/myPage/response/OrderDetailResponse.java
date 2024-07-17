package com.back.takeeat.dto.myPage.response;

import com.back.takeeat.domain.order.Order;
import com.back.takeeat.domain.order.OrderStatus;
import com.back.takeeat.dto.myPage.OrderMenuIdAndOptionCategoryId;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Builder
@Getter
public class OrderDetailResponse {

    private OrderStatus orderStatus;
    private String marketName;
    private LocalDateTime createdTime;
    private String orderCode;
    private String requirement;

    private List<OrderMenuResponse> orderMenuResponses;

    private Map<Long, List<OrderOptionCategoryResponse>> optionCategoryByOrderMenu;
    private Map<OrderMenuIdAndOptionCategoryId, List<OrderOptionResponse>> orderOptionMapByOptionCategoryId;

    public static OrderDetailResponse create(Order order, List<OrderMenuResponse> orderMenuResponses,
                                             Map<Long, List<OrderOptionCategoryResponse>> optionCategoryByOrderMenu,
                                             Map<OrderMenuIdAndOptionCategoryId, List<OrderOptionResponse>> orderOptionMapByOptionCategoryId) {
        return OrderDetailResponse.builder()
                .orderStatus(order.getOrderStatus())
                .marketName(order.getMarket().getMarketName())
                .createdTime(order.getCreatedTime())
                .orderCode(order.getOrderCode())
                .requirement(order.getRequirement())
                .orderMenuResponses(orderMenuResponses)
                .optionCategoryByOrderMenu(optionCategoryByOrderMenu)
                .orderOptionMapByOptionCategoryId(orderOptionMapByOptionCategoryId)
                .build();
    }

    public List<OrderOptionResponse> getOrderOptionInfo(Long orderMenuId, Long optionCategoryId) {
        OrderMenuIdAndOptionCategoryId oao = new OrderMenuIdAndOptionCategoryId(orderMenuId, optionCategoryId);
        return orderOptionMapByOptionCategoryId.get(oao);
    }

    public String getOrderDate() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 a hh시 mm분");
        return dateTimeFormatter.format(createdTime);
    }

    public String getOrderStatusKR() {
        if (this.orderStatus == OrderStatus.WAIT)
            return "주문 확인중";
        else if (this.orderStatus == OrderStatus.ACCEPT)
            return "메뉴 준비중";
        else if (this.orderStatus == OrderStatus.COMPLETE)
            return "픽업 완료";
        else
            return "주문 취소";
    }
}
