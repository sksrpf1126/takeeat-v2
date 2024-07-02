package com.back.takeeat.controller;

import com.back.takeeat.dto.notification.MarketMessageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Controller
public class NotificationController {

    @GetMapping("/notification/member")
    public String notificationMember() {
        return "notification/notificationMember";
    }

    @MessageMapping("/send-member/{userId}")
    @SendTo("/topic/notification-member/{userId}")
    public String memberNotification(@DestinationVariable("userId") Long userId, @RequestBody MarketMessageRequest messageRequest) {
        log.info("Market({marketId}) send a message to the member({userId})", messageRequest.getMarketId(), userId);
        return "hello User!";
    }

    @MessageMapping("/send-market/{marketId}")
    @SendTo("/topic/notification-market/{marketId}")
    public String marketNotification() {
        return "hello market!";
    }

}
