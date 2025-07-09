package com.admiral.adminshop.service;

import com.admiral.adminshop.dto.CreateUserDTO;
import com.admiral.adminshop.dto.ReadUserDTO;

public interface UserService {
    ReadUserDTO getUserByEmail(String username);
    ReadUserDTO registerAdmin(CreateUserDTO createUserDTO);
    boolean existsByEmail(String email);
}