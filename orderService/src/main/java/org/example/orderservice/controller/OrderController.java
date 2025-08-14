package org.example.orderservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.orderservice.entity.dto.OrderDto;
import org.example.orderservice.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders(@RequestParam Integer page,
                                                       @RequestParam Integer size) {
        List<OrderDto> all = orderService.findAllPage(page, size);
        return ResponseEntity.ok(all);
    }


}
