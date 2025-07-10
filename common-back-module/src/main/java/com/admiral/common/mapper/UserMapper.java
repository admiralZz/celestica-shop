package com.admiral.common.mapper;

import com.admiral.common.database.model.User;
import com.admiral.common.dto.CreateUserDTO;
import com.admiral.common.dto.ReadUserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    
    // Entity -> DTO
    ReadUserDTO toDTO(User user);

    User toEntity(CreateUserDTO createUserDTO);
}