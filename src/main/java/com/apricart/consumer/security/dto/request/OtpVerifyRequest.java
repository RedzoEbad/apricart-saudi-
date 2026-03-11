package com.apricart.consumer.security.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class OtpVerifyRequest {

    @NotEmpty()
    private String phoneNumber;

    private String password;


    @NotEmpty()
    private String otp;

}
