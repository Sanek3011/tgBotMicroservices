package org.example.userservice.util;

import lombok.experimental.UtilityClass;
import org.example.userservice.service.notification.NotificationRequest;


import java.util.List;

@UtilityClass
public class NotificationRequestFactory {

    public static NotificationRequest notificationForUserId(Long userId, String message) {
        return NotificationRequest.builder()
                .userId(userId)
                .message(message)
                .build();
    }

    public static NotificationRequest notificationForTgId(Long tgId, String message) {
        return NotificationRequest.builder()
                .tgId(tgId)
                .message(message)
                .build();
    }

    public static NotificationRequest notificationFull(Long userId, Long tgId, String message) {
        return NotificationRequest.builder()
                .userId(userId)
                .tgId(tgId)
                .message(message).build();
    }

}
