package com.back.takeeat.repository;

import com.back.takeeat.domain.option.OptionCategory;
import com.back.takeeat.domain.order.OrderOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderOptionRepository extends JpaRepository<OrderOption,Long> {

    @Query("SELECT DISTINCT oc FROM OptionCategory oc " +
            "JOIN FETCH oc.options o " +
            "JOIN FETCH OrderOption oo ON o.id = oo.option.id " +
            "WHERE oo.orderMenu.id = :orderMenuId")
    List<OptionCategory> findOrderOptionTest(@Param("orderMenuId") Long orderMenuId);
}
