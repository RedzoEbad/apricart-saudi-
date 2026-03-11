package com.apricart.consumer.security.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class CartResponseDTO {
    private Long id;
    private String quantity;
    private Long customerId;
    private Long productId;
    private Long productWarehouseId;
    private ProductResponseDTO product;

}
