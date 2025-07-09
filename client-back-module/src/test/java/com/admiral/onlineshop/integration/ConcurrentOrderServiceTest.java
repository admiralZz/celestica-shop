package com.admiral.onlineshop.integration;

import com.admiral.onlineshop.dto.*;
import com.admiral.onlineshop.exception.ProductNotFoundException;
import com.admiral.onlineshop.integration.util.TestHelper;
import com.admiral.onlineshop.model.Order;
import com.admiral.onlineshop.repository.OrderRepository;
import com.admiral.onlineshop.repository.ProductRepository;
import com.admiral.onlineshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@RequiredArgsConstructor
public class ConcurrentOrderServiceTest extends ConcurrentIntegrationTest {
    private final OrderService orderService;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final TestHelper testHelper;

    /**
     * Тест одновременного создания заказа
     *
     * @throws InterruptedException
     */
    @Test
    public void testConcurrentCreateOrder() throws InterruptedException {
        Long productId = 4L;
        int quantity = 15;
        var createOrderRequest1 = CreateOrderDTO.builder()
                .items(new CreateOrderItemDTO[]{
                        CreateOrderItemDTO
                                .builder()
                                .productId(productId)
                                .quantity(quantity)
                                .build()
                })
                .email("anatoliy@test.com")
                .phone("+7(123)123-123-123")
                .address("г. Москва, ул. Пышечная, д. 9, кв. 31")
                .build();
        var createOrderRequest2 = CreateOrderDTO.builder()
                .items(new CreateOrderItemDTO[]{
                        CreateOrderItemDTO
                                .builder()
                                .productId(productId)
                                .quantity(quantity)
                                .build()
                })
                .email("boris@test.com")
                .phone("+7(123)123-123-123")
                .address("г. Санкт-Петербург, ул. Шашлычная, д. 9, кв. 31")
                .build();
        Integer beforeStockQuantity = productRepository.findStockQuantityById(productId);
        log.info("Stock quantity before test start: {}", beforeStockQuantity);

        testHelper.runSynchronously(
                () -> orderService.createOrder(createOrderRequest1),
                () -> orderService.createOrder(createOrderRequest2)
        );

        Integer afterStockQuantity = productRepository.findStockQuantityById(productId);
        log.info("Stock quantity after test start: {}", afterStockQuantity);
        assertThat(afterStockQuantity).isEqualTo(0);

        List<Order> orders = orderRepository.findAll();
        log.info("All orders after test start: {}", orders.size());
        assertThat(orders).hasSize(1);
    }
}
