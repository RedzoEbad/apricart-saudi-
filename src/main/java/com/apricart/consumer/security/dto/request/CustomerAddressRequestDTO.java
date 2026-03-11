package com.apricart.consumer.security.dto.request;

import com.apricart.consumer.security.enums.AddressType;
import com.apricart.consumer.security.enums.LocationType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Builder
@Getter
@Setter
public class CustomerAddressRequestDTO {
    private Long id;
    private String addressDetail;
    private String addressLatitude;
    private String addressLongitude;
    private AddressType addressType;
    private LocationType locationType;
    private boolean isActive;
    private Long cityId;

    @JsonIgnore
    private LocalDateTime createDateTime;

}
