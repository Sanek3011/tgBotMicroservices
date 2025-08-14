package org.example.orderservice.service.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {

    private Long userId;
    private Long tgId;
    private String message;
    private String email;
    private List<String> roles;
}
