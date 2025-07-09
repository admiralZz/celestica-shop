package com.admiral.adminshop.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class ProductDTO {
    
    Long id;
    
    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    String name;
    
    String description;
    
    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be positive")
    BigDecimal price;
    
    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity must be positive")
    Integer stockQuantity;
    
    Long categoryId;
    
    // Optional field for read operations that includes the category name
    String categoryName;

    Long imageId;
} 