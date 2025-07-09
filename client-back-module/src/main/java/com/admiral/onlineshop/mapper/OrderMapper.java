package com.admiral.onlineshop.mapper;

import com.admiral.onlineshop.dto.OrderDTO;
import com.admiral.onlineshop.model.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = { OrderMapper.class })
public interface OrderMapper {
    // List<Entity> -> List<DTO>
    List<OrderDTO> toDTOList(List<Order> orders);

    OrderDTO toDto(Order entity);
} 