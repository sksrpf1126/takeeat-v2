package com.back.takeeat.domain.cart;

import com.back.takeeat.domain.menu.Menu;
import com.back.takeeat.domain.order.OrderOption;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_menu_id")
    private Long id;

    private int cartQuantity;

    private int cartMenuPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @OneToMany(mappedBy = "cartMenu", cascade = CascadeType.REMOVE)
    private List<CartOption> cartOptions;

    public void updateQuantity(int newQuantity) {
        this.cartQuantity = newQuantity;
    }

    public void addQuantity(int addQuantity) {
        this.cartQuantity += addQuantity;
    }

}
