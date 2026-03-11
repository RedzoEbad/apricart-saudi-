package com.apricart.consumer.security.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WarehouseResponseDTO {
    private Long id;
    private String name;
    private String arabicName;
    private boolean isActive;
    private String address;
    private String arabicAddress;
    private String latitude;
    private String longitude;
    private Long cityId;

}
