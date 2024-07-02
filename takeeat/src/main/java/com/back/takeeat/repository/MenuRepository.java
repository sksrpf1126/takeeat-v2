package com.back.takeeat.repository;

import com.back.takeeat.domain.menu.MenuCategory;
import com.back.takeeat.dto.marketMenu.response.OptionCategoryResponse;
import com.back.takeeat.dto.marketMenu.response.OptionResponse;
import com.back.takeeat.dto.marketMenu.response.MenuResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends JpaRepository<MenuCategory, Long> {

    @Query(
            "SELECT new com.back.takeeat.dto.marketMenu.response.MenuResponse(mc.id, mc.menuCategoryName, " +
                    "m.id, m.menuName, m.menuIntroduction, m.menuMaxCount, m.menuImage, m.menuPrice) " +
                    "FROM MenuCategory mc " +
                    "INNER JOIN mc.menus m " +
                    "WHERE mc.market.id = :marketId"
    )
    List<MenuResponse> findMenuByMarketId(@Param("marketId") Long marketId);

    @Query(
            "SELECT new com.back.takeeat.dto.marketMenu.response.OptionCategoryResponse(m.id, " +
                    "oc.id, oc.optionCategoryName, oc.optionMaxCount, oc.optionSelect) " +
                    "FROM MenuCategory mc " +
                    "INNER JOIN mc.menus m " +
                    "INNER JOIN m.optionCategories oc " +
                    "WHERE mc.market.id = :marketId"
    )
    List<OptionCategoryResponse> findOptionCategoryByMarketId(@Param("marketId") Long marketId);

    @Query(
            "SELECT new com.back.takeeat.dto.marketMenu.response.OptionResponse(oc.id, " +
                    "o.id, o.optionName, o.optionPrice) " +
                    "FROM MenuCategory mc " +
                    "INNER JOIN mc.menus m " +
                    "INNER JOIN m.optionCategories oc " +
                    "INNER JOIN oc.options o " +
                    "WHERE mc.market.id = :marketId"
    )
    List<OptionResponse> findOptionByMarketId(@Param("marketId") Long marketId);
}
