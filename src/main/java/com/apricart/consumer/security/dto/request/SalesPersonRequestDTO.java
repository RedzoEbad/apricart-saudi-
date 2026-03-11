package com.apricart.consumer.security.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@Builder
@ToString
public class SalesPersonRequestDTO {
    private Long id;
    @NotNull
    private String name;
    private String arabicName;
    private String email;
    private String arabicDescription;
    private String description;
    private Boolean isActive;
    private Long cityId;
}
