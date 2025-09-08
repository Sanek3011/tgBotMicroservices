package org.example.bff.service;

import lombok.RequiredArgsConstructor;
import org.example.bff.dto.HasUser;
import org.example.bff.service.outputService.UserOutputService;
import org.example.bff.tempStorage.TempStorage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class UserNameFiller <T extends HasUser> {

    private final TempStorage<String> tempStorage;
    private final UserOutputService outputService;

    public Mono<T> fillNames(T dto) {
        if (tempStorage.get(dto.getUserId()).isPresent()) {
            dto.setUserName(tempStorage.get(dto.getUserId()).get());
            return Mono.just(dto);
        }
        return outputService.getUsernames(Set.of(dto.getUserId()))
                .map(m -> {
                    String name = m.get(dto.getUserId());
                    dto.setUserName(name);
                    tempStorage.put(dto.getUserId(), name);
                    return dto;
                });
    }

    public Flux<T> fillNames(Flux<T> dtos) {
        return dtos.collectList()
                .flatMapMany(list -> {
                    Set<Long> collect = list.stream()
                            .filter(r -> r.getUserName() == null || r.getUserName().equals("Юзер не подгрузился"))
                            .map(HasUser::getUserId)
                            .filter(id -> tempStorage.get(id).isEmpty())
                            .collect(Collectors.toSet());

                    if (collect.isEmpty()) {
                        return Flux.fromIterable(fillFromCache(list));
                    }

                    return outputService.getUsernames(collect)
                            .flatMapMany(names -> Flux.fromIterable(list)
                                    .map(r -> {
                                        if (r.getUserName() == null || r.getUserName().equals("Юзер не подгрузился")) {
                                            r.setUserName(names.get(r.getUserId()));
                                        }
                                        tempStorage.put(r.getUserId(), r.getUserName());
                                        return r;
                                    }));
                });
    }

    private List<T> fillFromCache(List<T> dtos) {
        for (T dto : dtos) {
            dto.setUserName(tempStorage.get(dto.getUserId()).get());
        }
        return dtos;
    }
}
