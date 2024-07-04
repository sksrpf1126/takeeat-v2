package com.back.takeeat.repository;

import com.back.takeeat.domain.menu.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketMenuRepository extends JpaRepository<Menu, Long> {
}
