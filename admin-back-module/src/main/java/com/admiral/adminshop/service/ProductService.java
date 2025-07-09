package com.admiral.adminshop.service;

import com.admiral.adminshop.dto.product.ProductCreateDTO;
import com.admiral.adminshop.dto.product.ProductDTO;
import com.admiral.adminshop.dto.product.ProductUpdateDTO;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    List<ProductDTO> getAllProducts();
    ProductDTO getProductById(Long id);
    ProductDTO createProduct(ProductCreateDTO productDTO);
    ProductDTO updateProduct(Long id, ProductUpdateDTO productDTO);
    void deleteProduct(Long id);
    List<ProductDTO> getProductsByCategory(Long categoryId);
    List<ProductDTO> searchProductsByName(String name);
    List<ProductDTO> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    boolean isInStock(Long productId, Integer quantity);
}
