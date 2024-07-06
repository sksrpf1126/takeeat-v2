package com.back.takeeat.service;

import com.back.takeeat.common.exception.AuthException;
import com.back.takeeat.common.exception.EntityNotFoundException;
import com.back.takeeat.common.exception.ErrorCode;
import com.back.takeeat.domain.market.Market;
import com.back.takeeat.domain.menu.Menu;
import com.back.takeeat.domain.notification.Notification;
import com.back.takeeat.domain.option.Option;
import com.back.takeeat.domain.order.Order;
import com.back.takeeat.domain.order.OrderMenu;
import com.back.takeeat.domain.order.OrderOption;
import com.back.takeeat.domain.order.OrderStatus;
import com.back.takeeat.domain.user.Member;
import com.back.takeeat.dto.marketorder.response.MarketOrdersResponse;
import com.back.takeeat.dto.notification.request.MarketMessageRequest;
import com.back.takeeat.dto.notification.request.TestOrderMenuRequest;
import com.back.takeeat.dto.notification.request.TestOrderRequest;
import com.back.takeeat.dto.notification.response.ReceiveMessageResponse;
import com.back.takeeat.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final MemberRepository memberRepository;
    private final MarketOrderRepository marketOrderRepository;
    private final MarketRepository marketRepository;
    private final NotificationRepository notificationRepository;
    private final OptionRepository optionRepository;
    private final MenuRealRepository menuRepository;
    private final MarketOrderService marketOrderService;

    @Transactional
    public ReceiveMessageResponse registerNotification(Long memberId, MarketMessageRequest messageRequest) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        Market findMarket = marketRepository.findById(messageRequest.getMarketId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MARKET_NOT_FOUND));

        Notification notification = makeNotificationWithOrder(findMember, findMarket, messageRequest.getSelectOrderStatus());
        marketOrderService.updateOrderStatusWithTime(memberId, messageRequest.getOrderId(), messageRequest.getCurrentOrderStatus(), messageRequest.getSelectOrderStatus());

        return ReceiveMessageResponse.of(notificationRepository.save(notification));
    }

    @Transactional
    public MarketOrdersResponse registerMarketOrder(Long marketId, TestOrderRequest orderRequest) {
        Member findMember = memberRepository.findById(orderRequest.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        Market findMarket = marketRepository.findById(marketId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MARKET_NOT_FOUND));

        List<OrderMenu> orderMenus = createOrderMenus(orderRequest.getOrderMenuRequests());

        Order order = Order.builder()
                        .member(findMember)
                        .market(findMarket)
                        .requirement(orderRequest.getRequirement())
                        .totalPrice(orderRequest.getTotalPrice())
                        .build();

        orderMenus.forEach((orderMenu -> orderMenu.associateOrder(order)));
        marketOrderRepository.save(order);

        return MarketOrdersResponse.of(order);
    }

    @Transactional(readOnly = true)
    public List<ReceiveMessageResponse> findAllNotification(Long memberId) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        return ReceiveMessageResponse.listOf(notificationRepository.findByMemberId(memberId));
    }

    @Transactional
    public String readNotification(Long memberId, Long notificationId) {
        Notification findNotification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOTIFICATION_NOT_FOUND));

        validateNotificationOwner(memberId, findNotification);

        findNotification.notificationCheck();

        return "success";
    }

    @Transactional
    public void deleteNotification(Long memberId, Long notificationId) {
        Notification findNotification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOTIFICATION_NOT_FOUND));

        validateNotificationOwner(memberId, findNotification);

        notificationRepository.delete(findNotification);
    }

    @Transactional(readOnly = true)
    public Boolean existsNotificationsNotRead(Long memberId) {
        return notificationRepository.existsByMemberIdAndWatched(memberId, false);
    }

    private void validateNotificationOwner(Long memberId, Notification notification) {
        if(!memberId.equals(notification.getMember().getId())) {
            throw new AuthException(ErrorCode.NOTIFICATION_UNAUTHORIZED);
        }
    }


    private List<OrderMenu> createOrderMenus(List<TestOrderMenuRequest> orderMenuRequests) {
        return orderMenuRequests.stream()
                .map(this::createOrderMenuWithOptions)
                .collect(Collectors.toList());
    }

    private OrderMenu createOrderMenuWithOptions(TestOrderMenuRequest orderMenuRequest) {
        Menu findMenu = menuRepository.findById(orderMenuRequest.getMenuId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        List<OrderOption> orderOptions = getOrderOptions(orderMenuRequest.getOptionIds());

        OrderMenu orderMenu = OrderMenu.builder()
                .menu(findMenu)
                .orderQuantity(orderMenuRequest.getOrderQuantity())
                .build();

        orderOptions.forEach((orderOption -> orderOption.associateOrderMenu(orderMenu)));

        return orderMenu;
    }

    /**
     * @TODO OptionService가 만들어지면 optionRepository.findByIdIn을 통해 List<Option>을 반환하는 public 메서드 만들기
     */
    private List<OrderOption> getOrderOptions(List<Long> optionIds) {

        List<Option> options = optionRepository.findByIdIn(optionIds);

        return options.stream()
                        .map((option) -> OrderOption.builder()
                                            .option(option)
                                            .build()
                        )
                        .collect(Collectors.toList());
    }

    private Notification makeNotificationWithOrder(Member receiverMember, Market senderMarket, OrderStatus requestStatus) {

        String memberNickname = receiverMember.getNickname();
        String marketName = senderMarket.getMarketName();

        String title = "";
        String message = "";

        switch (requestStatus) {
            case REJECT:
                title = "주문 거절";
                message = String.format("안녕하세요, \"%s\"님. <br> \n\"%s\" 가맹점의 사정으로 인해 주문이 거절되었습니다. 불편을 드려 죄송합니다.",
                        memberNickname, marketName);
                break;
            case ACCEPT:
                title = "주문 접수";
                message = String.format("안녕하세요, \"%s\"님. <br> \n\"%s\" 가맹점에서 성공적으로 주문을 접수했습니다. 곧 준비가 시작됩니다. 감사합니다!",
                        memberNickname, marketName);
                break;
            case CANCEL:
                title = "주문 취소";
                message = String.format("안녕하세요, \"%s\"님. <br> \n\"%s\" 가맹점의 사정으로 인해 주문을 취소했습니다. 불편을 드려 죄송합니다.",
                        memberNickname, marketName);
                break;
            case COMPLETE:
                title = "포장 완료";
                message = String.format("안녕하세요, \"%s\"님. <br> \n\"%s\" 가맹점이 포장을 완료했습니다. 가져가서 맛있게 드셔주세요!",
                        memberNickname, marketName);
                break;
            default:
                title = "알림";
                message = "알 수 없는 주문 상태입니다.";
                break;
        }

        return Notification.builder().member(receiverMember).title(title).contents(message).build();
    }

}
