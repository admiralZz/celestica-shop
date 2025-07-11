package com.admiral.common.service;

import com.admiral.common.dto.product.ProductCreateDTO;
import com.admiral.common.dto.product.ProductDTO;
import com.admiral.common.dto.product.ProductUpdateDTO;
import com.admiral.common.exception.CategoryNotFoundException;
import com.admiral.common.exception.ProductNotFoundException;
import com.admiral.common.mapper.ProductMapper;
import com.admiral.common.database.model.Category;
import com.admiral.common.database.model.Product;
import com.admiral.common.database.repository.CategoryRepository;
import com.admiral.common.database.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductImageService productImageService;
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
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        return productMapper.toDTO(product);
    }

    @Override
    @Transactional
    public ProductDTO createProduct(ProductCreateDTO productCreateDTO, MultipartFile image) {
        // Преобразование DTO в Entity
        Product product = productMapper.toEntity(productCreateDTO);

        // Проверка существования категории
        setCategory(productCreateDTO::getCategoryName, product);

        // Обновление картинки, если указана
        Optional.ofNullable(image)
                .map(productImageService::upload)
                .ifPresent(product::setImage);

        // Сохранение продукта
        Product savedProduct = productRepository.save(product);

        // Преобразование обратно в DTO
        return productMapper.toDTO(savedProduct);
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long id, ProductUpdateDTO productUpdateDTO, MultipartFile image) {
        // Проверка существования продукта
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        // Обновление данных продукта из DTO
        productMapper.updateEntityFromDto(productUpdateDTO, existingProduct);

        // Обновление категории, если указана
        setCategory(productUpdateDTO::getCategoryName, existingProduct);

        // Обновление картинки, если указана
        Optional.ofNullable(image)
                .map(productImageService::upload)
                .ifPresent(existingProduct::setImage);

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
            throw new ProductNotFoundException("Cannot delete. Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + categoryId));
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

    private void setCategory(Supplier<String> getter,
                             Product product) {
        String categoryName = getter.get();
        if (categoryName != null) {
            Category category = categoryRepository.findByName(categoryName)
                    .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + categoryName));
            product.setCategory(category);
        } else {
            throw new CategoryNotFoundException("Category is not specified");
        }
    }
}