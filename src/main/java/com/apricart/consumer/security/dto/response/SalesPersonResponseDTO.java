package com.apricart.consumer.security.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class SalesPersonResponseDTO {
    private Long id;
    private String name;
    private String arabicName;
    private String email;
    private String arabicDescription;
    private String description;
    private Boolean isActive;
    private Long cityId;
}
