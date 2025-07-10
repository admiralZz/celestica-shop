package com.admiral.common.service;

import com.admiral.common.database.model.User;
import com.admiral.common.database.model.UserRole;
import com.admiral.common.database.repository.UserRepository;
import com.admiral.common.dto.CreateUserDTO;
import com.admiral.common.dto.ReadUserDTO;
import com.admiral.common.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

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
    public List<ReadUserDTO> getAllUsers() {
        return userRepository.findAllByRole(UserRole.ROLE_USER).stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public ReadUserDTO registerAdmin(CreateUserDTO createUserDTO) {
        return register(createUserDTO, UserRole.ROLE_ADMIN);
    }
    @Override
    @Transactional
    public ReadUserDTO registerUser(CreateUserDTO createUserDTO) {
        return register(createUserDTO, UserRole.ROLE_USER);
    }
    private ReadUserDTO register(CreateUserDTO createUserDTO, UserRole role) {
        // Проверка уникальности email
        if (userRepository.findByEmail(createUserDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // Преобразование DTO в Entity
        User user = userMapper.toEntity(createUserDTO);

        // Хэширование пароля
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Установка роли по умолчанию
        user.setRole(role);

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