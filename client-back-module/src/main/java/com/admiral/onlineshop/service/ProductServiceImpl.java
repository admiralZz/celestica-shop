package com.admiral.onlineshop.service;

import com.admiral.onlineshop.dto.*;
import com.admiral.onlineshop.mapper.ProductMapper;
import com.admiral.onlineshop.model.Category;
import com.admiral.onlineshop.model.Product;
import com.admiral.onlineshop.repository.CategoryRepository;
import com.admiral.onlineshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return productMapper.toDTOList(products);
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return productMapper.toDTO(product);
    }

    @Override
    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        // Преобразование DTO в Entity
        Product product = productMapper.toEntity(productDTO);

        // Проверка существования категории
        if (productDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + productDTO.getCategoryId()));
            product.setCategory(category);
        }

        // Сохранение продукта
        Product savedProduct = productRepository.save(product);

        // Преобразование обратно в DTO
        return productMapper.toDTO(savedProduct);
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        // Проверка существования продукта
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        // Обновление данных продукта из DTO
        productMapper.updateEntityFromDto(productDTO, existingProduct);

        // Обновление категории, если указана
        if (productDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + productDTO.getCategoryId()));
            existingProduct.setCategory(category);
        }

        // Сохранение обновленного продукта
        Product updatedProduct = productRepository.save(existingProduct);

        // Преобразование обратно в DTO
        return productMapper.toDTO(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        // Проверка существования продукта
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete. Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
        List<Product> products = productRepository.findByCategory(category);
        return productMapper.toDTOList(products);
    }

    @Override
    public List<ProductDTO> searchProductsByName(String name) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(name);
        return productMapper.toDTOList(products);
    }

    @Override
    public List<ProductDTO> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        List<Product> products = productRepository.findByPriceBetween(minPrice, maxPrice);
        return productMapper.toDTOList(products);
    }

    @Override
    public boolean isInStock(Long productId, Integer quantity) {
        return productRepository.findById(productId)
                .map(product -> product.getStockQuantity() >= quantity)
                .orElse(false);
    }
} 