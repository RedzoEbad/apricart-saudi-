package com.apricart.consumer.security.dto.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class RequestParametersDTO {


    @NotEmpty
    private String city;

    @NotEmpty
    private String serviceType;

}
