package org.example.service;


public interface PaginationService {
    void nextPage(Long tgId);
    void previousPage(Long tgId);
    int getCurrentPage(Long tgId);
    void resetPage(Long tgId);
}
