package com.apricart.consumer.security.dto.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OTPRequest {

    @NotNull
    private String phoneNumber;

}