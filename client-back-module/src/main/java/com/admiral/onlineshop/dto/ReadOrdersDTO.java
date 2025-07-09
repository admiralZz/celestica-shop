package com.admiral.onlineshop.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ReadOrdersDTO {
    List<OrderDTO> orders;
}
