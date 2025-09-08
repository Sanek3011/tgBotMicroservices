package org.example.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.example.orderservice.entity.Item;
import org.example.orderservice.entity.Order;
import org.example.orderservice.entity.Status;
import org.example.orderservice.entity.dto.OrderDto;
import org.example.orderservice.kafka.events.OrderRequestedEvent;
import org.example.orderservice.repository.OrderRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ItemService itemService;

    public Order saveOrder(OrderRequestedEvent event) {
        Item item = itemService.getItemByName(event.getItem());
        Order order = Order.builder()
                .item(item)
                .status(Status.PENDING)
                .date(LocalDateTime.now())
                .build();
        return orderRepository.save(order);
    }

    public List<OrderDto> findAllPage(Integer currentPage, Integer ordersPerPage) {
        List<Order> date;
        if (currentPage != null && ordersPerPage != null) {
            date = orderRepository.findActiveOrders(Set.of(Status.REJECTED, Status.PENDING, Status.DONE),
                    PageRequest.of(currentPage, ordersPerPage, Sort.by("date").ascending())).getContent();
        }else{
            date = orderRepository.findAll(Sort.by("date").ascending());
        }
        return date.stream()
                .map(order -> new OrderDto(order.getId(), order.getUserId(), order.getItem().getDesc(), order.getDate(), order.getStatus().toString()))
                .toList();
    }




    public Long saveOrder(Order order) {
        Order save = orderRepository.save(order);
        return save.getId();
    }

    public Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow();
    }

    public OrderDto findDtoById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow();
        return OrderDto.builder()
                .id(order.getId())
                .date(order.getDate())
                .itemDesc(order.getItem().getDesc())
                .status(order.getStatus().toString())
                .userId(order.getUserId())
                .build();
    }
}
