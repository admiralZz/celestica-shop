package com.admiral.onlineshop.service;

import com.admiral.onlineshop.dto.CreateUserDTO;
import com.admiral.onlineshop.dto.ReadUserDTO;

public interface UserService {
    ReadUserDTO registerUser(CreateUserDTO createUserDTO);
    ReadUserDTO getUserById(Long id);
    ReadUserDTO getUserByEmail(String username);
    boolean existsByEmail(String email);
}