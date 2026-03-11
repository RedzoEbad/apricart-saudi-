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
public class UpdatePasswordRequest {

    @NotEmpty()
    private String currentPassword;

    @NotEmpty()
    private String newPassword;

}
