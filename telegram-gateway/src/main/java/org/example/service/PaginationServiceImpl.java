package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.tempStorage.TempStorage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaginationServiceImpl implements PaginationService{

    private final TempStorage<Integer> tempStorage;

    @Override
    public int getCurrentPage(Long tgId) {
        return tempStorage.get(tgId).orElse(0);
    }

    @Override
    public void nextPage(Long tgId) {
        Integer tmp = tempStorage.get(tgId).orElse(0);
        tempStorage.put(tgId, ++tmp);
    }

    @Override
    public void previousPage(Long tgId) {
        Integer tmp = tempStorage.get(tgId).orElse(0);
        tempStorage.put(tgId, Math.max(--tmp, 0));
    }

    @Override
    public void resetPage(Long tgId) {
        tempStorage.remove(tgId);
    }
}
