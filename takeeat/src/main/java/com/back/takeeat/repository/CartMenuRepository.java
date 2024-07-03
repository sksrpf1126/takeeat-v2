package com.back.takeeat.repository;

import com.back.takeeat.domain.cart.CartMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartMenuRepository extends JpaRepository<CartMenu, Long> {
}
