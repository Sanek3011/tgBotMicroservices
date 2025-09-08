package org.example.userservice.entity;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    GUEST,
    USER,
    LEADER,
    ADMIN;


    public static Set<String> getAllRoles() {
        return Arrays.stream(Role.values())
                .map(Enum::name)
                .collect(Collectors.toSet());
    }
}
