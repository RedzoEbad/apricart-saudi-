package com.apricart.consumer.security.dto.request;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class WarehouseRequestDTO {
    private Long id;
    private String name;
    private String arabicName;
    private Boolean isActive;
    private String address;
    private String arabicAddress;
    private String latitude;
    private String longitude;
    private Long cityId;

}
