package com.admiral.adminshop.service;

import com.admiral.adminshop.database.model.User;
import com.admiral.adminshop.database.repository.OrderRepository;
import com.admiral.adminshop.database.repository.UserRepository;
import com.admiral.adminshop.dto.order.ReadOrdersDTO;
import com.admiral.adminshop.exception.UserAccessDeniedException;
import com.admiral.adminshop.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    @Override
    public ReadOrdersDTO getAllOrders() {
        return ReadOrdersDTO.builder()
                .orders(orderMapper.toDTOList(orderRepository.findAll()))
                .build();
    }

    @Override
    public ReadOrdersDTO getUserOrders(String email) {
        if (email == null || email.isEmpty() || email.isBlank()) {
            throw new UserAccessDeniedException("Email cannot be empty or null");
        }
        return userRepository.findByEmail(email)
                .map(User::getOrders)
                .map(orders -> ReadOrdersDTO.builder()
                        .orders(orderMapper.toDTOList(orders))
                        .build())
                .orElseThrow(() -> new UserAccessDeniedException("User by email " + email +
                        " not found or orders not found"));
    }
}
