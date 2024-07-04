package com.back.takeeat.domain.cart;

import com.back.takeeat.domain.market.Market;
import com.back.takeeat.domain.menu.Menu;
import com.back.takeeat.domain.option.Option;
import com.back.takeeat.domain.order.OrderMenu;
import com.back.takeeat.domain.user.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "market_id")
    private Market market;

    @OneToMany(mappedBy = "cart")
    private List<CartMenu> cartMenus;

    public void addFirstMenu(Market market) {
        this.market = market;
    }

    public void deleteLastMenu() {
        this.market = null;
    }

}
