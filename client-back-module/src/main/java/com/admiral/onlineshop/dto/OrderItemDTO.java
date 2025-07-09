package com.admiral.onlineshop.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class OrderItemDTO {
    ProductDTO product;
    BigDecimal total;
    Integer quantity;
}
