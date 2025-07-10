package com.admiral.common.dto.order;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class OrderDTO {
    OrderItemDTO[] items;
    String email;
    String phone;
    String address;
    BigDecimal total;
}
