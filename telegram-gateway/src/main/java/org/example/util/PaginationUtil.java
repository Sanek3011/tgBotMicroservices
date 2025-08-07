package org.example.util;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PaginationUtil {
    private final Map<Long, Integer> chatPages = new ConcurrentHashMap<>();

    public PaginationUtil() {
    }

    public int getCurrentPage(Long chatId) {
        return chatPages.getOrDefault(chatId, 0);
    }
    public void nextPage(Long chatId) {
        chatPages.put(chatId, getCurrentPage(chatId) + 1);
    }

    public void previousPage(Long chatId) {
        chatPages.put(chatId, Math.max(0, getCurrentPage(chatId) - 1));
    }
    public void resetPage(Long chatId) {
        chatPages.put(chatId, 0);
    }
}
