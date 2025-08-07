package org.example.tempStorage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TempStorageManager {

    private final List<TempStorage<?>> storageList;

    public void clearForUser(Long tgId) {
        storageList.forEach(s -> s.remove(tgId));
    }

}
