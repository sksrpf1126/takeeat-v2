package com.back.takeeat.domain.order;

import com.back.takeeat.common.domain.BaseTimeEntity;
import com.back.takeeat.domain.market.Market;
import com.back.takeeat.domain.payment.Payment;
import com.back.takeeat.domain.user.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    private String requirement;

    private int totalPrice;

    private boolean checking;

    private LocalDateTime acceptedTime;

    private LocalDateTime completedTime;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "market_id")
    private Market market;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST})
    private List<OrderMenu> orderMenus = new ArrayList<>();

    @Builder
    public Order(Member member, Market market, String requirement, int totalPrice) {
        this.member = member;
        this.market = market;
        this.requirement = requirement;
        this.totalPrice = totalPrice;
        this.orderStatus = OrderStatus.WAIT;
        this.checking = false;
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void updateAcceptOrCompleteTime(OrderStatus orderStatus) {
        if(orderStatus.equals(OrderStatus.ACCEPT)) {
            this.acceptedTime = LocalDateTime.now();
        }else if(orderStatus.equals(OrderStatus.COMPLETE)) {
            this.completedTime = LocalDateTime.now();
        }
    }

    public void deleteOrderMenu(OrderMenu orderMenu) {
        this.orderMenus.remove(orderMenu);
    }

    public void addOrderMenu(OrderMenu orderMenu) {
        this.orderMenus.add(orderMenu);
    }

    public void orderCheck() {
        this.checking = true;
    }

}
