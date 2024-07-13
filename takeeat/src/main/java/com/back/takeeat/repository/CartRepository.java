package com.back.takeeat.repository;

import com.back.takeeat.domain.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByMemberId(@Param("memberId") Long memberId);

    @Query(
            "SELECT c " +
            "FROM Cart c OUTER JOIN FETCH c.market m " +
            "OUTER JOIN FETCH c.cartMenus cm " +
            "OUTER JOIN FETCH cm.menu mu " +
            "WHERE c.id = :memberId"
    )
    Optional<Cart> findByMemberIdWithMenu(@Param("memberId") Long memberId);
}
