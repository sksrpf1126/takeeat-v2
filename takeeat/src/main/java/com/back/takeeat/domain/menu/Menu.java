package com.back.takeeat.domain.menu;

import jakarta.persistence.*;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    @Column(length = 1000)
    private String menuIntroduction;

    private int menuMaxCount;

    private String menuImage;

    private int menuPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_category_id")
    private MenuCategory menuCategory;

}
