package com.back.takeeat.service;

import com.back.takeeat.common.exception.AuthException;
import com.back.takeeat.common.exception.EntityNotFoundException;
import com.back.takeeat.common.exception.ErrorCode;
import com.back.takeeat.domain.market.Market;
import com.back.takeeat.domain.notification.Notification;
import com.back.takeeat.domain.order.OrderStatus;
import com.back.takeeat.domain.user.Member;
import com.back.takeeat.dto.notification.request.MarketMessageRequest;
import com.back.takeeat.dto.notification.response.ReceiveMessageResponse;
import com.back.takeeat.repository.MarketRepository;
import com.back.takeeat.repository.MemberRepository;
import com.back.takeeat.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final MemberRepository memberRepository;
    private final MarketRepository marketRepository;
    private final NotificationRepository notificationRepository;
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
