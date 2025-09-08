package org.example.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.example.orderservice.entity.Item;
import org.example.orderservice.kafka.events.ItemProcessEvent;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemProcessService {

    private final ItemService itemService;

    public void processItem(ItemProcessEvent event) {
        switch (event.getEventType()) {
            case "update":
                updateItem(event);
                break;
            case "save":
                save(event);
                break;
            case "delete":
                delete(event);
                break;
        }

    }

    public void updateItem(ItemProcessEvent event) {
        Item item = itemService.findById(event.getId());
        item.setDesc(event.getDesc());
        item.setPrice(event.getPrice());
        itemService.save(item);
    }

    public void save(ItemProcessEvent event) {
        Item item = Item.builder()
                .price(event.getPrice())
                .desc(event.getDesc())
                .type(event.getType())
                .build();
        itemService.save(item);
    }

    public void delete(ItemProcessEvent event) {
        itemService.removeById(event.getId());
    }
}
