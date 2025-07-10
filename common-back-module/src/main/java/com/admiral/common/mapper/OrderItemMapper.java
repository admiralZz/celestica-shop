package com.admiral.common.mapper;

import com.admiral.common.dto.order.OrderItemDTO;
import com.admiral.common.database.model.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    OrderItemDTO toDto(OrderItem entity);
} 