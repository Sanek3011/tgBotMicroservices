package org.example.userservice.controller;


import lombok.RequiredArgsConstructor;
import org.example.userservice.kafka.events.UserRoleEvent;
import org.example.userservice.service.UserService;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@org.springframework.web.bind.annotation.RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class RestController {

    private final UserService userService;

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
}
