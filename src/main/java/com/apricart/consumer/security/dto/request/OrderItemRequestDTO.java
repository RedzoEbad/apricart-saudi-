package com.apricart.consumer.security.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemRequestDTO {
    private Long id;
    private String title;
    private String arabicTitle;
    private String quantity;
    private Double taxAmount;
    private Double totalAmount;
    private String orderId;
    private Long productWarehouseId;

}
