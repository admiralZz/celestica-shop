package com.admiral.common.dto.order;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.Instant;

@Value
@Builder
public class OrderDTO {
    OrderItemDTO[] items;
    String email;
    String phone;
    String address;
    Instant createdAt;
    BigDecimal total;
}
