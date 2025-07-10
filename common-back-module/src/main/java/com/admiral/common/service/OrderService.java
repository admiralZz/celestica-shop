package com.admiral.common.service;

import com.admiral.common.dto.order.*;

public interface OrderService {
    ReadOrdersDTO getAllOrders();
    ReadOrdersDTO getUserOrders(String email);
    CheckOrderDTO checkPreOrder(CreateCheckOrderDTO createCheckOrderDTO);
    OrderDTO createOrder(CreateOrderDTO orderDTO);
}
