package org.example.orderservice.util;

import lombok.experimental.UtilityClass;
import org.example.orderservice.service.notification.NotificationRequest;

import java.util.List;

@UtilityClass
public class NotificationRequestFactory {

    public static NotificationRequest notificationForRoles(String message, List<String> roles) {
        return NotificationRequest.builder()
                .message(message)
                .roles(roles)
                .build();
    }

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

}
