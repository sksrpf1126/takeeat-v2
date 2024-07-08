package com.back.takeeat.repository;

import com.back.takeeat.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(
            "SELECT o " +
            "FROM Order o INNER JOIN FETCH o.market " +
            "INNER JOIN FETCH o.orderMenus om " +
            "INNER JOIN FETCH om.menu " +
            "OUTER JOIN FETCH o.review " +
            "WHERE o.member.id = :memberId " +
            "ORDER BY o.id DESC"
    )
    List<Order> findByMemberIdForOrderList(@Param("memberId") Long memberId);

}
