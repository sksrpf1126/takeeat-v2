package com.back.takeeat.domain.menu;

import com.back.takeeat.domain.market.Market;
import jakarta.persistence.*;
import lombok.Builder;

@Entity
@Builder
public class MenuCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_category_id")
    private Long id;

    private String menuCategoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "market_id")
    private Market market;

}
