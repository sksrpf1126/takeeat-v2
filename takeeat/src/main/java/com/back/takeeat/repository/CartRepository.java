package com.back.takeeat.repository;

import com.back.takeeat.domain.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByMemberId(@Param("memberId") Long memberId);

    @Query(
            "SELECT c " +
                    "FROM Cart c INNER JOIN FETCH c.market m " +
                    "INNER JOIN FETCH c.cartMenus cm " +
                    "INNER JOIN FETCH cm.menu mu " +
                    "WHERE c.id = :memberId"
    )
    Cart findByMemberIdWithMenu(@Param("memberId") Long memberId);
}
