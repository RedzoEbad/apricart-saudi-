package com.apricart.consumer.security.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CouponDetailResponseDTO {
    private Long id;
    private Long customerId;
    private String phoneNumber;
    private Long warehouseId;
    private String orderId;
    private Long couponId;
}