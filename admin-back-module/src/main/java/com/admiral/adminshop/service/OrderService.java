package com.admiral.adminshop.service;

import com.admiral.adminshop.dto.order.ReadOrdersDTO;

public interface OrderService {
    ReadOrdersDTO getAllOrders();
    ReadOrdersDTO getUserOrders(String email);
}
