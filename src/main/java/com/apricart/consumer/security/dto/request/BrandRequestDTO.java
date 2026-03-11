package com.apricart.consumer.security.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BrandRequestDTO {
    private Long id;
    private String name;
    private String arabicName;
    private Boolean status;
    private String image;
}
