package com.back.takeeat.dto.notification.response;

import com.back.takeeat.domain.notification.Notification;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ReceiveMessageResponse {

    private Long notificationId;
    private String title;
    private String contents;
    private Boolean watched;
    private LocalDateTime createdTime;

    public static ReceiveMessageResponse of(Notification notification) {
        return ReceiveMessageResponse.builder()
                .notificationId(notification.getId())
                .title(notification.getTitle())
                .contents(notification.getContents())
                .watched(notification.getWatched())
                .createdTime(notification.getCreatedTime())
                .build();
    }

    public static List<ReceiveMessageResponse> listOf(List<Notification> notifications) {
        return notifications.stream()
                .map(ReceiveMessageResponse::of)
                .collect(Collectors.toList());
    }

}
