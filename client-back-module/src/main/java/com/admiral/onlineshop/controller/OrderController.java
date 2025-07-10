package com.admiral.onlineshop.controller;

import com.admiral.common.dto.order.*;
import com.admiral.common.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<ReadOrdersDTO> getOrdersByUser(Authentication authentication) {
        ReadOrdersDTO userOrders = orderService.getUserOrders(authentication.getName());
        return ResponseEntity.ok(userOrders);
    }

    @PostMapping("/check")
    public ResponseEntity<CheckOrderDTO> checkPreOrder(@Valid @RequestBody CreateCheckOrderDTO createCheckOrderDTO) {
        CheckOrderDTO result = orderService.checkPreOrder(createCheckOrderDTO);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/create")
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody CreateOrderDTO createOrderDTO, Authentication authentication) {
        OrderDTO result = orderService.createOrder(createOrderDTO);
        return ResponseEntity.ok(result);
    }
}
