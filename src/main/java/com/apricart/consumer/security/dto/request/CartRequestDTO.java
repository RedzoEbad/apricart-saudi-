package com.apricart.consumer.security.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class CartRequestDTO {
    private Long id;
    private String quantity;
    private Long customerId;
    private Long productId;
    private Long productWarehouseId;
}
