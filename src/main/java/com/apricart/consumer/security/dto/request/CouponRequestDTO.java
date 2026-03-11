package com.apricart.consumer.security.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CouponRequestDTO {
    private Long id;
    private String code;
    private Double couponDiscount;
    private String description;
    private String arabicDescription;
    private Double minSubTotal;
    private Date expiry;
    private String usageLimit;
    private Boolean status;
}
