package com.admiral.common.dto.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
// для десериализации DTO с Value и Builder в json. Например чтобы можно было юзать эти сущности в контроллерах
@Jacksonized
public class CreateCheckOrderDTO {

    @NotNull(message = "Items to order is required")
    @Size(min = 1, message = "At least one item to order")
    CreateOrderItemDTO[] items;
}
