package com.admiral.onlineshop.service;

import com.admiral.onlineshop.dto.*;

public interface OrderService {
    ReadOrdersDTO getUserOrders(String email);
    CheckOrderDTO checkPreOrder(CreateCheckOrderDTO createCheckOrderDTO);
    OrderDTO createOrder(CreateOrderDTO orderDTO);
}
