package com.apricart.consumer.security.dto.response;

import com.apricart.consumer.security.enums.OrderType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PendingOrderResponseDTO {
    private Long id;
    private String orderId;
    private Boolean status;
}
