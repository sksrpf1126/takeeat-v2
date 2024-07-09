package com.back.takeeat.dto.myPage.response;

import com.back.takeeat.domain.order.Order;
import com.back.takeeat.domain.order.OrderMenu;
import com.back.takeeat.domain.order.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
public class OrderListResponse {

    private Long orderId;
    private int totalPrice;
    private OrderStatus orderStatus;
    private LocalDateTime createdTime;

    private Long marketId;
    private String marketName;
    private String marketImage;

    private Long reviewId;

    private String menuName;
    private int menuNumber;

    public static OrderListResponse createByOrder(Order order) {
        OrderListResponseBuilder builder = OrderListResponse.builder()
                .orderId(order.getId())
                .totalPrice(order.getTotalPrice())
                .orderStatus(order.getOrderStatus())
                .createdTime(order.getCreatedTime())
                .marketId(order.getMarket().getId())
                .marketName(order.getMarket().getMarketName())
                .marketImage(order.getMarket().getMarketImage())
                .menuName(order.getOrderMenus().get(0).getMenu().getMenuName());

        if (order.getReview() != null) {
            builder.reviewId(order.getReview().getId());
        }

        int totalQuantity = 0;
        for (OrderMenu orderMenu : order.getOrderMenus()) {
            totalQuantity += orderMenu.getOrderQuantity();
        }
        builder.menuNumber(totalQuantity - 1); //'외'를 위해 이름을 출력하는 메뉴 제외

        return builder.build();
    }

    public String getOrderDate() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
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
