package com.admiral.common.dto.order;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class CheckOrderDTO {
    OrderItemDTO[] items;
    BigDecimal totalPrice;
}
