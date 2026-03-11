package com.apricart.consumer.security.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CouponResponseDTO {
        private Long id;
        private String code;
        private Double couponDiscount;
        private String description;
        private String arabicDescription;
        private Double minSubTotal;
        private Date expiry;
        private String usageLimit;
        private Boolean status;
//        private Long couponDetailId;
}
