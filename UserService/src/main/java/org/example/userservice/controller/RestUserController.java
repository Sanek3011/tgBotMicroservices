package org.example.userservice.controller;


import lombok.RequiredArgsConstructor;
import org.example.userservice.dto.UserDto;
import org.example.userservice.dto.UserTgVisualDto;
import org.example.userservice.kafka.events.UserRoleEvent;
import org.example.userservice.service.UserService;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class RestUserController {

    private final UserService userService;

    @GetMapping("/users/count")
    public Long countUsers() {
        return userService.getCountUsers();
    }

    @GetMapping("users/tg")
    public ResponseEntity<List<UserTgVisualDto>> getAllUsersForTg() {
        List<UserTgVisualDto> allUsersForTg = userService.getAllUsersForTg();
        return ResponseEntity.ok(allUsersForTg);
    }

    @GetMapping("/roles/non-guest")
    public ResponseEntity<List<UserRoleEvent>> getNonGuestUsers() {
        List<UserRoleEvent> nonGuestUsers = userService.getNonGuestUsers();
        return ResponseEntity.ok(nonGuestUsers);
    }

    @GetMapping("/balance/{id}")
    public ResponseEntity<Integer> getBalance(@PathVariable Long id) {
        Optional<Integer> balance = userService.getBalance(id);
        if (balance.isEmpty()) {
            return ResponseEntity.notFound().build();
        }else {
            return ResponseEntity.ok(balance.get());
        }
    }

    @PostMapping("/users/names")
    public ResponseEntity<Map<Long, String>> getNamesById(@RequestBody Set<Long> ids) {
        Map<Long, String> allNamesByIds = userService.getAllNamesByIds(ids);
        return ResponseEntity.ok(allNamesByIds);
    }

    @GetMapping("/balance")
    public ResponseEntity<Integer> getBalanceByName(@RequestParam(required = true) String name) {
        Integer balance = userService.getBalanceByName(name);
        return ResponseEntity.ok(balance);
    }
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/roles")
    public ResponseEntity<Set<String>> getAllRoles() {
        return ResponseEntity.ok(userService.getAllRoles());
    }
}
