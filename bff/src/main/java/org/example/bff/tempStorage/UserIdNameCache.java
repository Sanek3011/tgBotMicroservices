package org.example.bff.tempStorage;

import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserIdNameCache implements TempStorage <String>{

    private final ConcurrentHashMap<Long, String> map = new ConcurrentHashMap<>();

    @Override
    public Optional<String> get(Long userId) {
        return Optional.ofNullable(map.get(userId));
    }

    @Override
    public void put(Long userId, String value) {
        map.put(userId, value);
    }

    @Override
    public void remove(Long userId) {
        map.remove(userId);

    }
}
