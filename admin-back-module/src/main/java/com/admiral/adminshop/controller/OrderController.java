package com.admiral.adminshop.controller;

import com.admiral.adminshop.dto.order.ReadOrdersDTO;
import com.admiral.adminshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<ReadOrdersDTO> getOrders() {
        ReadOrdersDTO userOrders = orderService.getAllOrders();
        return ResponseEntity.ok(userOrders);
    }
}
