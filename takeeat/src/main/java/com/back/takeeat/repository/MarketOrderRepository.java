package com.back.takeeat.repository;

import com.back.takeeat.domain.order.Order;
import com.back.takeeat.domain.order.OrderStatus;
import com.back.takeeat.dto.marketorder.response.OrdersCountResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MarketOrderRepository extends JpaRepository<Order, Long> {

    @Query(
            "SELECT new com.back.takeeat.dto.marketorder.response.OrdersCountResponse(o.orderStatus, COUNT(o.orderStatus)) " +
            "FROM Order o " +
            "WHERE o.market.id = :marketId " +
            "GROUP BY o.orderStatus"
    )
    List<OrdersCountResponse> findOrdersCounting(@Param("marketId") Long marketId);

    @Query(
            "SELECT o " +
            "FROM Order o INNER JOIN FETCH o.orderMenus " +
            "WHERE o.market.id = :marketId " +
            "AND o.orderStatus = :orderStatus"
    )
    List<Order> findAllWithOrderMenus(@Param("marketId") Long marketId, @Param("orderStatus") OrderStatus orderStatus);

}
