package com.admiral.onlineshop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@Value
@Builder
@ToString(of = {"email", "phone"})
public class CreateOrderDTO {
    @NotNull(message = "Items to order is required")
    @Size(min = 1, message = "At least one item to order")
    CreateOrderItemDTO[] items;
    @NotBlank(message = "Email is required")
    String email;
    @NotBlank(message = "Phone is required")
    String phone;
    @NotBlank(message = "Shipping address is required")
    String address;
}
