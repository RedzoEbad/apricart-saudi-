package com.apricart.consumer.security.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class RoleLinkRequest {

    @NotEmpty(message = "{registration_phone_number_not_empty}")
    private String phoneNumber;

    @NotNull(message= "{ROLE_ID_NOT_NULL}")
    private Long roleId;

}
