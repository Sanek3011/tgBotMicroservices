package org.example.tempStorage;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class IntegerTempStorage implements TempStorage<Integer>{

    private final Map<Long, Integer> map = new ConcurrentHashMap<>();
    @Override
    public Optional<Integer> get(Long userId) {
        return Optional.ofNullable(map.get(userId));
    }

    @Override
    public void put(Long userId, Integer value) {
        map.put(userId, value);
    }

    @Override
    public void remove(Long userId) {
        map.remove(userId);
    }

    public void setPage(String action, Long tgId) {
        Integer tmp = get(tgId).orElse(0);
        switch (action) {
            case "nextPage":
                map.put(tgId, ++tmp);
                break;
            case "previousPage":
                map.put(tgId, Math.max(--tmp, 0));
        }
    }
}
