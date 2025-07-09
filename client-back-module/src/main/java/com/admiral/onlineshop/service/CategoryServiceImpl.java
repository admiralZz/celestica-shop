package com.admiral.onlineshop.service;

import com.admiral.onlineshop.dto.CategoryDTO;
import com.admiral.onlineshop.mapper.CategoryMapper;
import com.admiral.onlineshop.model.Category;
import com.admiral.onlineshop.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.toDTOList(categories);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        return categoryMapper.toDTO(category);
    }

    @Override
    @Transactional
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        // Преобразование DTO в Entity
        Category category = categoryMapper.toEntity(categoryDTO);
        
        // Сохранение категории
        Category savedCategory = categoryRepository.save(category);
        
        // Преобразование обратно в DTO
        return categoryMapper.toDTO(savedCategory);
    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(CategoryDTO categoryDTO) {
        // Проверка существования категории
        Long id = categoryDTO.getId();
        if (id == null) {
            throw new RuntimeException("Category ID cannot be null for update operation");
        }
        
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cannot update. Category not found with id: " + id));
        
        // Обновление данных категории
        categoryMapper.updateEntityFromDto(categoryDTO, existingCategory);
        
        // Сохранение обновленной категории
        Category updatedCategory = categoryRepository.save(existingCategory);
        
        // Преобразование обратно в DTO
        return categoryMapper.toDTO(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        // Проверка существования категории
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete. Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }
} 