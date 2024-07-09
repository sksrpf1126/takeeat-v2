package com.back.takeeat.repository;

import com.back.takeeat.domain.order.OrderMenu;
import com.back.takeeat.dto.cart.response.CartOptionResponse;
import com.back.takeeat.dto.myPage.response.OrderOptionCategoryResponse;
import com.back.takeeat.dto.myPage.response.OrderOptionResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderMenuRepository extends JpaRepository<OrderMenu, Long> {

    @Query(
            "SELECT DISTINCT new com.back.takeeat.dto.myPage.response.OrderOptionCategoryResponse(om.id, oc.id, oc.optionCategoryName) " +
            "FROM OrderMenu om INNER JOIN om.orderOptions oo " +
            "INNER JOIN oo.option o " +
            "INNER JOIN o.optionCategory oc " +
            "WHERE om.order.id = :orderId " +
            "GROUP BY om.id, oc.id"
    )
    List<OrderOptionCategoryResponse> findByOrderIdWithOptionCategory(@Param("orderId") Long orderId);

    @Query(
            "SELECT new com.back.takeeat.dto.myPage.response.OrderOptionResponse(om.id, oc.id, o.optionName, o.optionPrice) " +
            "FROM OrderMenu om INNER JOIN om.orderOptions oo " +
            "INNER JOIN oo.option o " +
            "INNER JOIN o.optionCategory oc " +
            "WHERE om.order.id = :orderId"
    )
    List<OrderOptionResponse> findByOrderIdWithOption(@Param("orderId") Long orderId);
}
