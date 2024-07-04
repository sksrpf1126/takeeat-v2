package com.back.takeeat.repository;


import com.back.takeeat.domain.market.Market;
import com.back.takeeat.domain.menu.MenuCategory;
import com.back.takeeat.dto.market.request.MarketMenuCategoryRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuCategoryRepository extends JpaRepository<MenuCategory, Long> {

}
