package com.admiral.onlineshop.integration;

import com.admiral.onlineshop.dto.*;
import com.admiral.onlineshop.exception.ProductNotFoundException;
import com.admiral.onlineshop.repository.ProductRepository;
import com.admiral.onlineshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@RequiredArgsConstructor
public class OrderServiceTest extends IntegrationTest {
    private final OrderService orderService;
    private final ProductRepository productRepository;

    @Test
    public void testPreCreateOrder() {

        var createOrderRequest = CreateCheckOrderDTO.builder()
                .items(new CreateOrderItemDTO[]{
                        CreateOrderItemDTO
                                .builder()
                                .productId(1L)
                                .quantity(2)
                                .build(),
                        CreateOrderItemDTO
                                .builder()
                                .productId(4L)
                                .quantity(1)
                                .build()
                })
                .build();
        CheckOrderDTO checkOrderDTO = orderService.checkPreOrder(createOrderRequest);
        assertThat(checkOrderDTO).isNotNull();
        assertThat(checkOrderDTO.getTotalPrice()).isEqualByComparingTo("1999.97");
    }

    @Test
    public void testCreateOrder() {
        Long productId1 = 1L;
        Long productId2 = 4L;
        int quantity1 = 2;
        int quantity2 = 1;
        var createOrderRequest = CreateOrderDTO.builder()
                .items(new CreateOrderItemDTO[]{
                        CreateOrderItemDTO
                                .builder()
                                .productId(productId1)
                                .quantity(quantity1)
                                .build(),
                        CreateOrderItemDTO
                                .builder()
                                .productId(productId2)
                                .quantity(quantity2)
                                .build()
                })
                .email("test@test.com")
                .phone("+7(123)123-123-123")
                .address("г. Москва, ул. Пышечная, д. 9, кв. 31")
                .build();
        Integer beforeStockQuantity1 = productRepository.findStockQuantityById(productId1);
        Integer beforeStockQuantity2 = productRepository.findStockQuantityById(productId2);
        OrderDTO order = orderService.createOrder(createOrderRequest);
        assertThat(order).isNotNull();
        assertThat(order.getTotal()).isEqualByComparingTo("1999.97");

        Integer afterStockQuantity1 = productRepository.findStockQuantityById(productId1);
        Integer afterStockQuantity2 = productRepository.findStockQuantityById(productId2);
        assertThat(afterStockQuantity1).isEqualTo(beforeStockQuantity1 - quantity1);
        assertThat(afterStockQuantity2).isEqualTo(beforeStockQuantity2 - quantity2);
    }

    @Test
    public void testCreateOrderNonExistProduct() {
        var createOrderRequest = CreateOrderDTO.builder()
                .items(new CreateOrderItemDTO[]{
                        CreateOrderItemDTO
                                .builder()
                                .productId(999999L)
                                .quantity(1)
                                .build()
                })
                .email("test@test.com")
                .phone("+7(123)123-123-123")
                .address("г. Москва, ул. Пышечная, д. 9, кв. 31")
                .build();
        assertThrows(ProductNotFoundException.class,
                () -> orderService.createOrder(createOrderRequest));
    }
}
