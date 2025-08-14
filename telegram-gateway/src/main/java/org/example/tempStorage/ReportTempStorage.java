package org.example.tempStorage;

import org.example.entity.dto.ReportAdminDto;
import org.example.kafka.events.ReportCreateEvent;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ReportTempStorage implements TempStorage<ReportCreateEvent>{

    private final Map<Long, ReportCreateEvent> map = new ConcurrentHashMap<>();

    @Override
    public Optional<ReportCreateEvent> get(Long userId) {
        return Optional.ofNullable(map.get(userId));
    }

    @Override
    public void put(Long userId, ReportCreateEvent value) {
        map.put(userId, value);
    }

    @Override
    public void remove(Long userId) {
        map.remove(userId);
    }
}
