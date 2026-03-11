package com.apricart.consumer.security.dto.response;

import com.apricart.consumer.security.enums.AddressType;
import com.apricart.consumer.security.enums.LocationType;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class CustomerAddressResponseDTO {
    private Long id;
    private String addressDetail;
    private String addressLatitude;
    private String addressLongitude;
    private AddressType addressType;
    private LocationType locationType;
    private boolean isActive;
    private Long cityId;
    private Long customerId;


}
