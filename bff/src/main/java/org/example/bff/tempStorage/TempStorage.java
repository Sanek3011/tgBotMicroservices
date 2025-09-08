package org.example.bff.tempStorage;

import java.util.Optional;

public interface TempStorage <T> {
    void put(Long userId, T value);
    Optional<T> get(Long userId);
    void remove(Long userId);
}
