package com.admiral.onlineshop.mapper;

import com.admiral.onlineshop.dto.CreateUserDTO;
import com.admiral.onlineshop.dto.ReadUserDTO;
import com.admiral.onlineshop.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    
    // Entity -> DTO
    ReadUserDTO toDTO(User user);
    
    // DTO -> Entity для создания нового пользователя
    @Mapping(target = "id", ignore = true)  // ID назначается базой данных
    @Mapping(target = "role", ignore = true) // Роль присваивается в сервисе
    User toEntity(CreateUserDTO createUserDTO);
} 