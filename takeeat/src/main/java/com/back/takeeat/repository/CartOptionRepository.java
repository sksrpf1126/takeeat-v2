package com.back.takeeat.repository;

import com.back.takeeat.domain.cart.CartOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartOptionRepository extends JpaRepository<CartOption, Long> {
}
