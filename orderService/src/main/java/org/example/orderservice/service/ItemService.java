package org.example.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.example.orderservice.entity.Item;
import org.example.orderservice.entity.dto.ItemDto;
import org.example.orderservice.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public List<ItemDto> getAllItems() {
        return itemRepository.findAll()
                .stream()
                .map(m -> new ItemDto(m.getId(), m.getDesc(), m.getType(), m.getPrice()))
                .toList();
    }

    public Item getItemByName(String name) {
        return itemRepository.findByType(name);
    }


}
