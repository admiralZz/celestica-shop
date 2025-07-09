package com.admiral.onlineshop.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class CheckOrderDTO {
    OrderItemDTO[] items;
    BigDecimal totalPrice;
}
