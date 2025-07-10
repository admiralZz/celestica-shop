package com.admiral.common.service;

import com.admiral.common.dto.product.ProductCreateDTO;
import com.admiral.common.dto.product.ProductDTO;
import com.admiral.common.dto.product.ProductUpdateDTO;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    List<ProductDTO> getAllProducts();
    ProductDTO getProductById(Long id);
    ProductDTO createProduct(ProductCreateDTO productDTO, MultipartFile image);
    ProductDTO updateProduct(Long id, ProductUpdateDTO productDTO, MultipartFile image);
    void deleteProduct(Long id);
    List<ProductDTO> getProductsByCategory(Long categoryId);
    List<ProductDTO> searchProductsByName(String name);
    List<ProductDTO> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    boolean isInStock(Long productId, Integer quantity);
}