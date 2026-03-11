package com.apricart.consumer.security.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CityResponseDTO {
    private Long id;
    private String name;
    private String country;
    private String arabicName;
    private String arabicCountry;
    private String image;
    private Boolean isActive;
    private List<WarehouseResponseDTO> warehouseDetails;
}
