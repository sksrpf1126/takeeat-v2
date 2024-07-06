package com.back.takeeat.controller;

import com.back.takeeat.dto.marketorder.response.MarketOrdersResponse;
import com.back.takeeat.dto.notification.request.MarketMessageRequest;
import com.back.takeeat.dto.notification.request.TestOrderRequest;
import com.back.takeeat.dto.notification.response.ReceiveMessageResponse;
import com.back.takeeat.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/notification/member")
    public String notificationMember(Model model) {
        //@Todo 해당 페이지를 접근할 때 로그인한 사용자가 존재하는 경우에만 읽지않은 알림 여부 로직 호출
        Long memberId = 1L;
        if(memberId != null) {
            model.addAttribute("notifiactionsReadYn", notificationService.existsNotificationsNotRead(memberId));
        }
        return "notification/notificationMember";
    }

    @GetMapping("/notification/{memberId}/exists")
    @ResponseBody
    public Boolean notificationMemberExists(@PathVariable("memberId") Long memberId) {
        return notificationService.existsNotificationsNotRead(memberId);
    }

    @MessageMapping("/send-member/{memberId}")
    @SendTo("/topic/notification-member/{memberId}")
    public ReceiveMessageResponse memberNotification(@DestinationVariable("memberId") Long memberId, @RequestBody MarketMessageRequest messageRequest) {
        log.info("Market({marketId}) send a message to the member({memberId})", messageRequest.getMarketId(), memberId);
        ReceiveMessageResponse receiveMessageResponse = notificationService.registerNotification(memberId, messageRequest);
        messagingTemplate.convertAndSend("/topic/reply/" + messageRequest.getMarketId(), "Message processed successfully");
        return receiveMessageResponse;
    }

    @MessageMapping("/send-market/{marketId}")
    @SendTo("/topic/notification-market/{marketId}")
    public MarketOrdersResponse marketNotification(@DestinationVariable("marketId") Long marketId, @RequestBody TestOrderRequest orderRequest) {
        log.info("Member({memberId}) send a message to the Market({marketId})", orderRequest.getMemberId(), marketId);

        return notificationService.registerMarketOrder(marketId, orderRequest);
    }

    @GetMapping("/notification/scroll/{memberId}")
    @ResponseBody
    public List<ReceiveMessageResponse> notificationScroll(@PathVariable("memberId") Long memberId) {

        return notificationService.findAllNotification(memberId);
    }

    @PostMapping("/notification/{notificationId}/read")
    @ResponseBody
    public String notificationRead(@PathVariable("notificationId") Long notificationId) {
        //@TODO 알림을 읽는 자가 본인의 알림인지 판단할 것
        Long memberId = 1L;

        return notificationService.readNotification(memberId, notificationId);
    }

    @DeleteMapping("/notification/{notificationId}/delete")
    @ResponseBody
    public void notificationDelete(@PathVariable("notificationId") Long notificationId) {
        //@TODO 알림을 삭제하는 자가 본인의 알림인지 판단할 것
        Long memberId = 1L;

        notificationService.deleteNotification(memberId, notificationId);
    }

}
