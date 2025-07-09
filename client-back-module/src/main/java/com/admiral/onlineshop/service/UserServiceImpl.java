package com.admiral.onlineshop.service;

import com.admiral.onlineshop.dto.CreateUserDTO;
import com.admiral.onlineshop.dto.ReadUserDTO;
import com.admiral.onlineshop.mapper.UserMapper;
import com.admiral.onlineshop.model.User;
import com.admiral.onlineshop.model.UserRole;
import com.admiral.onlineshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public ReadUserDTO registerUser(CreateUserDTO createUserDTO) {

        // Проверка уникальности email
        if (userRepository.findByEmail(createUserDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // Преобразование DTO в Entity
        User user = userMapper.toEntity(createUserDTO);
        
        // Хэширование пароля
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Установка роли по умолчанию
        user.setRole(UserRole.ROLE_USER);

        // Сохранение в базу данных
        User savedUser = userRepository.save(user);
        
        // Преобразование обратно в DTO
        return userMapper.toDTO(savedUser);
    }

    @Override
    public ReadUserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return userMapper.toDTO(user);
    }

    @Override
    public ReadUserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + email));
        return userMapper.toDTO(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}