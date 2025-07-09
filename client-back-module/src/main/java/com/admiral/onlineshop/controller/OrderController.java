package com.admiral.onlineshop.controller;

import com.admiral.onlineshop.dto.*;
import com.admiral.onlineshop.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@Slf4j
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
