package com.back.takeeat.domain.option;

import com.back.takeeat.domain.menu.Menu;
import jakarta.persistence.*;
import lombok.Builder;

@Entity
@Builder
public class OptionCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_category_id")
    private Long id;

    private String optionCategoryName;

    private int optionMaxCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

}
