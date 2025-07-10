package com.admiral.adminshop.service;

import com.admiral.adminshop.dto.CreateUserDTO;
import com.admiral.adminshop.dto.ReadUserDTO;

import java.util.List;

public interface UserService {
    ReadUserDTO getUserByEmail(String username);
    List<ReadUserDTO> getAllUsers();
    ReadUserDTO registerAdmin(CreateUserDTO createUserDTO);
    boolean existsByEmail(String email);
}