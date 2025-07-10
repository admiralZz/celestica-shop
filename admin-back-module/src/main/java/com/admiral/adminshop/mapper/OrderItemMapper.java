package com.admiral.adminshop.mapper;

import com.admiral.adminshop.database.model.OrderItem;
import com.admiral.adminshop.dto.order.OrderItemDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    OrderItemDTO toDto(OrderItem entity);
} 