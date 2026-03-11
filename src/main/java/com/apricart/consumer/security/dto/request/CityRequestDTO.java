package com.apricart.consumer.security.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CityRequestDTO {

    private Long id;
    private String name;
    private String country;
    private String arabicName;
    private String arabicCountry;
    private Boolean isActive;
}