package com.back.takeeat.dto.notification.response;

import com.back.takeeat.domain.notification.Notification;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReceiveMessageResponse {

    private Long notificationId;
    private String title;
    private String contents;

    public static ReceiveMessageResponse of(Notification notification) {
        return ReceiveMessageResponse.builder()
                .notificationId(notification.getId())
                .title(notification.getTitle())
                .contents(notification.getContents())
                .build();
    }

}
