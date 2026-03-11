package com.apricart.consumer.security.dto.request;

import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.security.enums.StatusType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MissingProductRequestDTO {
    private Long id;
    private String name;
    private String description;
    private Integer quantity;
    private String image;
    private StatusType productStatus;
    private LanguageType language;
    private Long customerId;
}
