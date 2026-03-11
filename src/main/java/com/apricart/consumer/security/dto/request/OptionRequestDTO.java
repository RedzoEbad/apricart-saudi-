package com.apricart.consumer.security.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OptionRequestDTO {
    private Long id;
    private String key;
    private String value;
    private Boolean status;
}