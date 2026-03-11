package com.apricart.consumer.security.dto.response;

import com.apricart.consumer.security.dto.dto.ProductDetailDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WishListResponseDTO {
    private Long id;
    private Long customerId;
    private Long productId;
    private ProductDetailDTO productDetail;
}
