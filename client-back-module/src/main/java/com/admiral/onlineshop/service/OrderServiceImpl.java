package com.admiral.onlineshop.service;

import com.admiral.onlineshop.dto.*;
import com.admiral.onlineshop.exception.InsufficientStockException;
import com.admiral.onlineshop.exception.ProductNotFoundException;
import com.admiral.onlineshop.exception.UserNotFoundException;
import com.admiral.onlineshop.mapper.OrderMapper;
import com.admiral.onlineshop.mapper.ProductMapper;
import com.admiral.onlineshop.model.Order;
import com.admiral.onlineshop.model.OrderItem;
import com.admiral.onlineshop.model.Product;
import com.admiral.onlineshop.model.User;
import com.admiral.onlineshop.repository.OrderRepository;
import com.admiral.onlineshop.repository.ProductRepository;
import com.admiral.onlineshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductMapper productMapper;
    private final OrderMapper orderMapper;

    @Override
    public ReadOrdersDTO getUserOrders(String email) {
        if (email == null || email.isEmpty() || email.isBlank()) {
            throw new UserNotFoundException("Email cannot be empty or null");
        }
        return userRepository.findByEmail(email)
                .map(User::getOrders)
                .map(orders -> ReadOrdersDTO.builder()
                        .orders(orderMapper.toDTOList(orders))
                        .build())
                .orElseThrow(() -> new UserNotFoundException("User by email " + email +
                        " not found or orders not found"));
    }

    @Override
    public CheckOrderDTO checkPreOrder(CreateCheckOrderDTO checkOrderDTO) {
        // На случай если придет несколько позиций одного и того же товара
        OrderItemDTO[] orderItems = retrieveOrderItems(checkOrderDTO.getItems());

        BigDecimal totalPrice = calculateTotal(orderItems);

        return CheckOrderDTO.builder()
                .items(orderItems)
                .totalPrice(totalPrice)
                .build();
    }

    @Transactional
    @Override
    public OrderDTO createOrder(CreateOrderDTO createOrder) {
        log.info("Creating order for: {}", createOrder);
        // На случай если придет несколько позиций одного и того же товара
        CreateOrderItemDTO[] mergedOrderItems = mergeOrderItems(createOrder.getItems());
        List<OrderItem> orderItems = Arrays.stream(mergedOrderItems)
                .map(item -> {
                    Product product = productRepository.findByIdForUpdate(item.getProductId())
                            .orElseThrow(() -> new ProductNotFoundException("Product by id = " +
                                    item.getProductId() +
                                    "not found"));

                    if (product.getStockQuantity() < item.getQuantity()) {
                        log.error("Insufficient stock for product {}. Order: {}", product, createOrder);
                        throw new InsufficientStockException("Insufficient stock for product: " + product);
                    }

                    product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
                    productRepository.save(product);

                    return OrderItem.builder()
                            .product(product)
                            .quantity(item.getQuantity())
                            .total(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                            .build();
                })
                .toList();

        // Вычисляем итог
        BigDecimal total = orderItems.stream()
                .map(OrderItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        // Формируем заказ
        Order order = Order.builder()
                .items(orderItems)
                .email(createOrder.getEmail())
                .phone(createOrder.getPhone())
                .address(createOrder.getAddress())
                .total(total)
                .build();
        orderItems.forEach(orderItem -> orderItem.setOrder(order));

        // Если есть пользователь с таким email привязываем к нему заказ
        Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .ifPresentOrElse(authentication -> userRepository.findByEmail(authentication.getName())
                                .ifPresentOrElse(order::setUser,
                                        () -> userRepository.findByEmail(createOrder.getEmail())
                                                .ifPresent(order::setUser)),
                        () -> userRepository.findByEmail(createOrder.getEmail())
                                .ifPresent(order::setUser));

        Order result = orderRepository.save(order);

        return orderMapper.toDto(result);
    }

    private OrderItemDTO[] retrieveOrderItems(CreateOrderItemDTO[] createOrderItemDTOs) {
        return Arrays.stream(createOrderItemDTOs)
                .collect(Collectors.toMap(
                        CreateOrderItemDTO::getProductId,
                        CreateOrderItemDTO::getQuantity,
                        Integer::sum))
                .entrySet().stream()
                // Далее создаем позиции заказа(товар + кол-во)
                .map(entry -> productRepository.findById(entry.getKey())
                        .map(productMapper::toDTO)
                        .map(productDTO -> OrderItemDTO.builder()
                                .product(productDTO)
                                .quantity(entry.getValue())
                                .total(productDTO
                                        .getPrice()
                                        .multiply(BigDecimal.valueOf(entry.getValue())))
                                .build())
                        .orElse(null))
                .filter(Objects::nonNull)
                .toArray(OrderItemDTO[]::new);
    }

    private CreateOrderItemDTO[] mergeOrderItems(CreateOrderItemDTO[] createOrderItemDTOs) {
        return Arrays.stream(createOrderItemDTOs)
                .collect(Collectors.toMap(
                        CreateOrderItemDTO::getProductId,
                        CreateOrderItemDTO::getQuantity,
                        Integer::sum))
                .entrySet()
                .stream()
                .map(entry -> CreateOrderItemDTO.builder()
                        .productId(entry.getKey())
                        .quantity(entry.getValue())
                        .build())
                .toArray(CreateOrderItemDTO[]::new);
    }

    private BigDecimal calculateTotal(OrderItemDTO[] orderItems) {
        return Arrays.stream(orderItems)
                .map(OrderItemDTO::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
