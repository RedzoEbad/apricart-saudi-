package com.apricart.consumer.security.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class DeliveryTimeResponseDTO {
    private Long id;
    private String deliveryTime;
    private Boolean status;
}
