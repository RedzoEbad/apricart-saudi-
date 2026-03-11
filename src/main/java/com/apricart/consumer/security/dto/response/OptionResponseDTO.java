package com.apricart.consumer.security.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OptionResponseDTO {
    private Long id;
    private String key;
    private String value;
    private Boolean status;
}
