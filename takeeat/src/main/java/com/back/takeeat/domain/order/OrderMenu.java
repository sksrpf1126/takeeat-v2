package com.back.takeeat.domain.order;

import com.back.takeeat.domain.menu.Menu;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_menu_id")
    private Long id;

    private int orderQuantity;

    private int orderPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @OneToMany(mappedBy = "orderMenu", cascade = {CascadeType.PERSIST})
    private List<OrderOption> orderOptions = new ArrayList<>();

    @Builder
    public OrderMenu(int orderQuantity, int orderPrice, Menu menu) {
        this.orderQuantity = orderQuantity;
        this.orderPrice = orderPrice;
        this.menu = menu;
    }

    public void associateOrder(Order order) {
        if(this.order != null) {
            order.deleteOrderMenu(this);
        }

        order.addOrderMenu(this);
        this.order = order;
    }

    public void addOrderOption(OrderOption orderOption) {
        this.orderOptions.add(orderOption);
        this.orderPrice += orderOption.getOption().getOptionPrice();
    }

    public void deleteOrderOption(OrderOption orderOption) {
        this.orderOptions.remove(orderOption);
    }

}
