package com.back.takeeat.controller;

import com.back.takeeat.domain.user.Member;
import com.back.takeeat.dto.marketorder.response.MarketOrdersResponse;
import com.back.takeeat.dto.notification.request.MarketMessageRequest;
import com.back.takeeat.dto.notification.response.ReceiveMessageResponse;
import com.back.takeeat.security.LoginMember;
import com.back.takeeat.security.oauth.PrincipalDetails;
import com.back.takeeat.service.MemberService;
import com.back.takeeat.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;
    private final MemberService memberService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/notification/member")
    @ResponseBody
    public Boolean notificationMember(@LoginMember Member member) {

        return notificationService.existsNotificationsNotRead(member.getId());
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
    public MarketOrdersResponse marketNotification(
            PrincipalDetails principalDetails,
            @DestinationVariable("marketId") Long marketId,
            @RequestBody MarketOrdersResponse ordersResponse) {

        Member loginMember = principalDetails.getMember();

        if(loginMember == null || loginMember.getId() == null) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }

        return ordersResponse;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/notification/scroll")
    @ResponseBody
    public List<ReceiveMessageResponse> notificationScroll(@LoginMember Member member) {

        return notificationService.findAllNotification(member.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/notification/{notificationId}/read")
    @ResponseBody
    public String notificationRead(@LoginMember Member member, @PathVariable("notificationId") Long notificationId) {

        return notificationService.readNotification(member.getId(), notificationId);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/notification/{notificationId}/delete")
    @ResponseBody
    public void notificationDelete(@LoginMember Member member, @PathVariable("notificationId") Long notificationId) {

        notificationService.deleteNotification(member.getId(), notificationId);
    }

}
