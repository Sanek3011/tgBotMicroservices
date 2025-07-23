package org.example.userservice.controller;


import lombok.RequiredArgsConstructor;
import org.example.userservice.kafka.events.UserRoleEvent;
import org.example.userservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/roles")
public class RoleRestController {

    private final UserService userService;

    @GetMapping("/non-guest")
    public ResponseEntity<List<UserRoleEvent>> getNonGuestUsers() {
        List<UserRoleEvent> nonGuestUsers = userService.getNonGuestUsers();
        return ResponseEntity.ok(nonGuestUsers);
    }
}
