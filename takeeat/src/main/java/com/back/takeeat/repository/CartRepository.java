package com.back.takeeat.repository;

import com.back.takeeat.domain.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByMemberId(@Param("memberId") Long memberId);
}
