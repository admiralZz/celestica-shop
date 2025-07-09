package com.admiral.onlineshop.mapper;

import com.admiral.onlineshop.dto.ProductDTO;
import com.admiral.onlineshop.model.Category;
import com.admiral.onlineshop.model.Product;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    
    // Entity -> DTO с извлечением имени категории
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "imageId", source = "image.id")
    ProductDTO toDTO(Product product);
    
    // List<Entity> -> List<DTO>
    List<ProductDTO> toDTOList(List<Product> products);
    
    // DTO -> Entity для создания нового продукта
    @Mapping(target = "id", ignore = true) // ID назначается базой данных
    @Mapping(target = "category", ignore = true) // Category устанавливается в сервисе
    Product toEntity(ProductDTO productDTO);
    
    // Метод для обновления категории в продукте
    @AfterMapping
    default void setCategoryIfNeeded(ProductDTO dto, @MappingTarget Product product) {
        if (dto.getCategoryId() != null) {
            Category category = new Category();
            category.setId(dto.getCategoryId());
            product.setCategory(category);
        }
    }
    
    // Обновление существующей сущности данными из DTO
    @Mapping(target = "id", ignore = true) // ID не должен меняться
    @Mapping(target = "category", ignore = true) // Category устанавливается через метод выше
    void updateEntityFromDto(ProductDTO productDTO, @MappingTarget Product product);
} 