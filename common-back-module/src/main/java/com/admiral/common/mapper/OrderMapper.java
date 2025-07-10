package com.admiral.common.mapper;

import com.admiral.common.database.model.Order;
import com.admiral.common.dto.order.OrderDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class })
public interface OrderMapper {
    // List<Entity> -> List<DTO>
    List<OrderDTO> toDTOList(List<Order> orders);

    OrderDTO toDto(Order entity);
} 