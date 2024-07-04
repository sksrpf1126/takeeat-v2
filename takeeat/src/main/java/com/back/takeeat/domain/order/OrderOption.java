package com.back.takeeat.domain.order;

import com.back.takeeat.domain.option.Option;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_option_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_menu_id")
    private OrderMenu orderMenu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id")
    private Option option;

    @Builder
    public OrderOption(Option option) {
        this.option = option;
    }

    public void associateOrderMenu(OrderMenu orderMenu) {
        if(this.orderMenu != null) {
            orderMenu.deleteOrderOption(this);
        }

        orderMenu.addOrderOption(this);
        this.orderMenu = orderMenu;
    }
}
