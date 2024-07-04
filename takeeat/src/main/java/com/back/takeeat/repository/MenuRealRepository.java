package com.back.takeeat.repository;

import com.back.takeeat.domain.menu.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRealRepository extends JpaRepository<Menu, Long> {
}
