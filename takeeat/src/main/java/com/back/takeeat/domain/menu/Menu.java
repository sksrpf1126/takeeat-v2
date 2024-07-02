package com.back.takeeat.domain.menu;

import com.back.takeeat.domain.option.OptionCategory;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    private String menuName;

    @Column(length = 1000)
    private String menuIntroduction;

    private int menuMaxCount;

    private String menuImage;

    private int menuPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_category_id")
    private MenuCategory menuCategory;

    @OneToMany(mappedBy = "menu")
    private List<OptionCategory> optionCategories;

}
