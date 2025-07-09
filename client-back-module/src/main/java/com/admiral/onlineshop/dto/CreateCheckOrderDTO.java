package com.admiral.onlineshop.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateCheckOrderDTO {

    @NotNull(message = "Items to order is required")
    @Size(min = 1, message = "At least one item to order")
    CreateOrderItemDTO[] items;
}
