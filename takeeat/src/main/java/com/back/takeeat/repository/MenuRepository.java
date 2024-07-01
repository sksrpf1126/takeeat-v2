package com.back.takeeat.repository;

import com.back.takeeat.domain.menu.MenuCategory;
import com.back.takeeat.dto.marketMenu.response.MenuResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends JpaRepository<MenuCategory, Long> {

    @Query(
            "SELECT new com.back.takeeat.dto.marketMenu.response.MenuResponse(mc.id, mc.menuCategoryName, " +
                    "m.id, m.menuName, m.menuIntroduction, m.menuMaxCount, m.menuImage, m.menuPrice, " +
                    "oc.id, oc.optionCategoryName, oc.optionMaxCount, oc.optionSelect, " +
                    "o.id, o.optionName, o.optionPrice) " +
                    "FROM MenuCategory mc " +
                    "INNER JOIN mc.menus m " +
                    "INNER JOIN m.optionCategories oc " +
                    "INNER JOIN oc.options o " +
                    "WHERE mc.market.id = :marketId"
    )
    List<MenuResponse> findMenusById(@Param("marketId") Long marketId);
}
