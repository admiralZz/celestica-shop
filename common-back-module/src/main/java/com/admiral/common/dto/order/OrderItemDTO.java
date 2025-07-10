package com.admiral.common.dto.order;

import com.admiral.common.dto.product.ProductDTO;
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
