package com.apricart.consumer.security.dto.dto;

import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.security.enums.StatusType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MissingProductDTO{

    private Long id;
    private String name;
    private String description;
    private Integer quantity;
    private String image;
    private StatusType productStatus;
    private LanguageType language;
    private Long customerId;

}
