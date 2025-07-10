package com.admiral.adminshop.dto.order;

import com.admiral.adminshop.dto.product.ProductDTO;
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
