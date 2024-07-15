package com.back.takeeat.repository;

import com.back.takeeat.domain.cart.CartMenu;
import com.back.takeeat.dto.cart.response.CartOptionCategoryResponse;
import com.back.takeeat.dto.cart.response.CartOptionResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartMenuRepository extends JpaRepository<CartMenu, Long> {

    @Query(
            "SELECT DISTINCT new com.back.takeeat.dto.cart.response.CartOptionCategoryResponse(cm.id, oc.id, oc.optionCategoryName) " +
            "FROM CartMenu cm INNER JOIN cm.cartOptions co " +
            "INNER JOIN co.option o " +
            "INNER JOIN o.optionCategory oc " +
            "WHERE cm.cart.id = :cartId " +
            "GROUP BY cm.id, oc.id"
    )
    List<CartOptionCategoryResponse> findByCartIdWithOptionCategory(@Param("cartId") Long cartId);

    @Query(
            "SELECT new com.back.takeeat.dto.cart.response.CartOptionResponse(cm.id, oc.id, o.id, o.optionName, o.optionPrice) " +
            "FROM CartMenu cm INNER JOIN cm.cartOptions co " +
            "INNER JOIN co.option o " +
            "INNER JOIN o.optionCategory oc " +
            "WHERE cm.cart.id = :cartId"
    )
    List<CartOptionResponse> findByCartIdWithOption(@Param("cartId") Long cartId);

    void deleteByCartId(@Param("cartId") Long id);

    List<CartMenu> findAllByMenuId(@Param("menuId") Long menuId);
}
