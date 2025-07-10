package com.admiral.common.service;

import com.admiral.common.dto.CreateUserDTO;
import com.admiral.common.dto.ReadUserDTO;

import java.util.List;

public interface UserService {
    ReadUserDTO getUserById(Long id);
    ReadUserDTO getUserByEmail(String username);
    List<ReadUserDTO> getAllUsers();
    ReadUserDTO registerUser(CreateUserDTO createUserDTO);
    ReadUserDTO registerAdmin(CreateUserDTO createUserDTO);
    boolean existsByEmail(String email);
}