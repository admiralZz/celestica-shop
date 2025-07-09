package com.admiral.onlineshop.mapper;

import com.admiral.onlineshop.dto.OrderItemDTO;
import com.admiral.onlineshop.model.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    OrderItemDTO toDto(OrderItem entity);
} 