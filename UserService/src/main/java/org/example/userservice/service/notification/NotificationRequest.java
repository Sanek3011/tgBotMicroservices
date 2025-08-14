package org.example.userservice.service.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.userservice.entity.Role;

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
    private List<Role> roles;
}
