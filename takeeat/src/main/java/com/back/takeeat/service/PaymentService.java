package com.back.takeeat.service;

import com.back.takeeat.common.exception.BaseException;
import com.back.takeeat.common.exception.EntityNotFoundException;
import com.back.takeeat.common.exception.ErrorCode;
import com.back.takeeat.domain.market.Market;
import com.back.takeeat.domain.menu.Menu;
import com.back.takeeat.domain.option.Option;
import com.back.takeeat.domain.order.Order;
import com.back.takeeat.domain.order.OrderMenu;
import com.back.takeeat.domain.order.OrderOption;
import com.back.takeeat.domain.user.Member;
import com.back.takeeat.dto.marketorder.response.MarketOrdersResponse;
import com.back.takeeat.dto.payment.request.PaymentOrderMenuRequest;
import com.back.takeeat.dto.payment.request.PaymentOrderRequest;
import com.back.takeeat.repository.MarketOrderRepository;
import com.back.takeeat.repository.MarketRepository;
import com.back.takeeat.repository.MenuRepository;
import com.back.takeeat.repository.OptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final MarketOrderRepository marketOrderRepository;
    private final MarketRepository marketRepository;
    private final MenuRepository menuRepository;
    private final OptionRepository optionRepository;
    private final CartService cartService;

    public MarketOrdersResponse registerPayment(Member member, PaymentOrderRequest paymentOrderRequest) {

        Market findMarket = marketRepository.findById(paymentOrderRequest.getMarketId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MARKET_NOT_FOUND));

        List<Long> menuIds = paymentOrderRequest.getOrderMenuRequests().stream()
                .map(PaymentOrderMenuRequest::getMenuId)
                .toList();

        //가게에 존재하지 않는 메뉴가 존재하는 경우
        if(!marketRepository.hasAllMenus(findMarket.getId(), menuIds, menuIds.size())) {
            throw new BaseException(ErrorCode.ORDER_MENUS_MISMATCH);
        }

        List<OrderMenu> orderMenus = createOrderMenus(paymentOrderRequest.getOrderMenuRequests());

        int menuTotalPrice = orderMenus.stream().mapToInt((o) -> o.getOrderPrice() * o.getOrderQuantity()).sum();

        //결제요청에서 보낸 총 금액과 DB에 존재하는 메뉴와 메뉴 옵션들의 가격의 합과 다른 경우
        if(menuTotalPrice != paymentOrderRequest.getAmount()) {
            throw new BaseException(ErrorCode.PAYMENT_MONEY_NOT_EQUAL);
        }

        Order order = Order.builder()
                .member(member)
                .market(findMarket)
                .payment(paymentOrderRequest.toPayment(member))
                .requirement(paymentOrderRequest.getRequirement())
                .totalPrice(paymentOrderRequest.getAmount())
                .orderCode(paymentOrderRequest.getOrderCode())
                .build();

        orderMenus.forEach((orderMenu -> orderMenu.associateOrder(order)));
        marketOrderRepository.save(order);
        cartService.deleteAllCartMenu(member.getId());
        return MarketOrdersResponse.of(order);
    }

    private List<OrderMenu> createOrderMenus(List<PaymentOrderMenuRequest> orderMenuRequests) {
        return orderMenuRequests.stream()
                .map(this::createOrderMenuWithOptions)
                .collect(Collectors.toList());
    }

    private OrderMenu createOrderMenuWithOptions(PaymentOrderMenuRequest orderMenuRequest) {
        Menu findMenu = menuRepository.findById(orderMenuRequest.getMenuId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        List<OrderOption> orderOptions = getOrderOptions(orderMenuRequest.getOptionIds());

        OrderMenu orderMenu = OrderMenu.builder()
                .menu(findMenu)
                .orderPrice(findMenu.getMenuPrice())
                .orderQuantity(orderMenuRequest.getOrderQuantity())
                .build();

        orderOptions.forEach((orderOption -> orderOption.associateOrderMenu(orderMenu)));

        return orderMenu;
    }

    private List<OrderOption> getOrderOptions(List<Long> optionIds) {

        List<Option> options = optionRepository.findByIdIn(optionIds);

        return options.stream()
                .map((option) -> OrderOption.builder()
                        .option(option)
                        .build()
                )
                .collect(Collectors.toList());
    }

}
