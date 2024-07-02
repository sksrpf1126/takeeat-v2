package com.back.takeeat.domain.option;

import com.back.takeeat.domain.menu.Menu;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OptionCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_category_id")
    private Long id;

    private String optionCategoryName;

    private int optionMaxCount;

    @Enumerated(EnumType.STRING)
    private OptionSelect optionSelect;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @OneToMany(mappedBy = "optionCategory")
    private List<Option> options;

}
