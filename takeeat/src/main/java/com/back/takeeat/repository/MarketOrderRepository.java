package com.back.takeeat.repository;

import com.back.takeeat.domain.order.Order;
import com.back.takeeat.dto.marketorder.response.OrdersCountResponse;
import com.back.takeeat.repository.Marketorder.MarketOrderRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MarketOrderRepository extends JpaRepository<Order, Long>, MarketOrderRepositoryCustom {

    @Query(
            "SELECT new com.back.takeeat.dto.marketorder.response.OrdersCountResponse(o.orderStatus, COUNT(o.orderStatus)) " +
            "FROM Order o " +
            "WHERE o.market.id = :marketId " +
            "GROUP BY o.orderStatus"
    )
    List<OrdersCountResponse> findOrdersCounting(@Param("marketId") Long marketId);

    @Query(
            "SELECT o " +
            "FROM Order o INNER JOIN FETCH o.orderMenus om " +
            "INNER JOIN FETCH o.member m " +
            "INNER JOIN FETCH om.menu mu " +
            "WHERE o.id = :orderId "
    )
    Optional<Order> findWithOrderMenus(@Param("orderId") Long orderId);

    @Query(
            "SELECT o " +
                    "FROM Order o INNER JOIN FETCH o.member m " +
                    "INNER JOIN FETCH o.market mk " +
                    "INNER JOIN FETCH o.payment p " +
                    "WHERE o.id = :orderId " +
                    "AND m.id = :memberId "
    )
    Optional<Order> findOrderWithPayment(@Param("orderId") Long orderId, @Param("memberId") Long memberId);

}
