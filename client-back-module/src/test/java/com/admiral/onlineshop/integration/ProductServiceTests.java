package com.admiral.onlineshop.integration;

import com.admiral.onlineshop.dto.ProductDTO;
import com.admiral.onlineshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ProductServiceTests extends IntegrationTest {
    private final ProductService productService;

    @Test
    public void testGetAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();

        Assertions.assertNotNull(products);
        Assertions.assertFalse(products.isEmpty());
    }
}
