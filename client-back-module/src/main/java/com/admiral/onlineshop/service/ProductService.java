package com.admiral.onlineshop.service;

import com.admiral.onlineshop.dto.ProductDTO;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    List<ProductDTO> getAllProducts();
    ProductDTO getProductById(Long id);
    ProductDTO createProduct(ProductDTO productDTO);
    ProductDTO updateProduct(Long id, ProductDTO productDTO);
    void deleteProduct(Long id);
    List<ProductDTO> getProductsByCategory(Long categoryId);
    List<ProductDTO> searchProductsByName(String name);
    List<ProductDTO> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    boolean isInStock(Long productId, Integer quantity);
}