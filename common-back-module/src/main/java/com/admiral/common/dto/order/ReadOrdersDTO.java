package com.admiral.common.dto.order;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ReadOrdersDTO {
    List<OrderDTO> orders;
}
