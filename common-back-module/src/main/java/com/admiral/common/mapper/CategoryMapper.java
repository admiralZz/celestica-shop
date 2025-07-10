package com.admiral.common.mapper;

import com.admiral.common.dto.CategoryDTO;
import com.admiral.common.database.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    
    // Entity -> DTO
    CategoryDTO toDTO(Category category);
    
    // List<Entity> -> List<DTO>
    List<CategoryDTO> toDTOList(List<Category> categories);
    
    // DTO -> Entity для создания новой категории
    @Mapping(target = "id", ignore = true) // ID назначается базой данных
    Category toEntity(CategoryDTO categoryDTO);
    
    // Обновление существующей сущности данными из DTO
    @Mapping(target = "id", ignore = true) // ID не должен меняться
    void updateEntityFromDto(CategoryDTO categoryDTO, @MappingTarget Category category);
} 