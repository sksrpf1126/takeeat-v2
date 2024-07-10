package com.back.takeeat.domain.option;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;

@Entity
@Table(name = "options")
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private Long id;

    private String optionName;

    private int optionPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_category_id")
    private OptionCategory optionCategory;

    public void addOptionCategory(OptionCategory optionCategory) {
        this.optionCategory = optionCategory;
        if (optionCategory.getOptions() == null) {
            optionCategory.addOptions(new ArrayList<>()); // 리스트가 null인 경우 초기화
        }
        if (!optionCategory.getOptions().contains(this)) {
            optionCategory.getOptions().add(this);
        }
    }

}
