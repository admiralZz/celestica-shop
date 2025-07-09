package com.admiral.adminshop.mapper;

import com.admiral.adminshop.database.model.Category;
import com.admiral.adminshop.database.model.Product;
import com.admiral.adminshop.dto.product.ProductCreateDTO;
import com.admiral.adminshop.dto.product.ProductDTO;
import com.admiral.adminshop.dto.product.ProductUpdateDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

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

    // DTO -> Entity для создания нового продукта
    @Mapping(target = "id", ignore = true) // ID назначается базой данных
    @Mapping(target = "category", ignore = true) // Category устанавливается в сервисе
    @Mapping(target = "image", ignore = true) // Image устанавливается в сервисе
    Product toEntity(ProductCreateDTO productDTO);

    // Обновление существующей сущности данными из ProductUpdateDTO
    @Mapping(target = "id", ignore = true) // ID не должен меняться
    @Mapping(target = "category", ignore = true) // Category устанавливается в сервисе
    @Mapping(target = "image", ignore = true) // Image устанавливается в сервисе
    void updateEntityFromDto(ProductUpdateDTO productDTO, @MappingTarget Product product);
} 