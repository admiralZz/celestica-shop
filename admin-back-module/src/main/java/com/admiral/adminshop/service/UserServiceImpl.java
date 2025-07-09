package com.admiral.adminshop.service;

import com.admiral.adminshop.database.model.User;
import com.admiral.adminshop.database.model.UserRole;
import com.admiral.adminshop.database.repository.UserRepository;
import com.admiral.adminshop.dto.CreateUserDTO;
import com.admiral.adminshop.dto.ReadUserDTO;
import com.admiral.adminshop.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ReadUserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + email));
        return userMapper.toDTO(user);
    }

    @Override
    @Transactional
    public ReadUserDTO registerAdmin(CreateUserDTO createUserDTO) {

        // Проверка уникальности email
        if (userRepository.findByEmail(createUserDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // Преобразование DTO в Entity
        User user = userMapper.toEntity(createUserDTO);

        // Хэширование пароля
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Установка роли по умолчанию
        user.setRole(UserRole.ROLE_ADMIN);

        // Сохранение в базу данных
        User savedUser = userRepository.save(user);

        // Преобразование обратно в DTO
        return userMapper.toDTO(savedUser);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}