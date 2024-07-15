package com.back.takeeat.domain.menu;

import com.back.takeeat.domain.option.OptionCategory;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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

    private String menuImage;

    private int menuPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_category_id")
    private MenuCategory menuCategory;

    @OneToMany(mappedBy = "menu")
    private List<OptionCategory> optionCategories;

    public void addMenuImage(String menuImage){
        this.menuImage = menuImage;
    }

    public void addMenuCategory(MenuCategory menuCategory) {
        this.menuCategory = menuCategory;
        if (menuCategory.getMenus() == null) {
            menuCategory.addMenus(new ArrayList<>()); // 리스트가 null인 경우 초기화
        }
        if (!menuCategory.getMenus().contains(this)) {
            menuCategory.getMenus().add(this);
        }
    }
}
