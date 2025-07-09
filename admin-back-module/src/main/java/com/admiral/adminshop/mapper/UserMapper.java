package com.admiral.adminshop.mapper;

import com.admiral.adminshop.database.model.User;
import com.admiral.adminshop.dto.CreateUserDTO;
import com.admiral.adminshop.dto.ReadUserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    
    // Entity -> DTO
    ReadUserDTO toDTO(User user);

    User toEntity(CreateUserDTO createUserDTO);
}