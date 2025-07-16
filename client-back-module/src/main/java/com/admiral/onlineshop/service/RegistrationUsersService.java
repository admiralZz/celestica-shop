package com.admiral.onlineshop.service;

import com.admiral.common.dto.CreateUserDTO;
import com.admiral.common.dto.ReadUserDTO;

public interface RegistrationUsersService {

    ReadUserDTO register(CreateUserDTO createUserDTO);
    ReadUserDTO confirm(String token);
}
