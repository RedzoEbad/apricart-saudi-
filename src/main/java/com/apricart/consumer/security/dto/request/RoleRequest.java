package com.apricart.consumer.security.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class RoleRequest {

    private Long id;

    @NotNull(message= "{ROLE_NAME_NOT_NULL}")
    @Pattern(regexp="^[a-zA-Z ]*$",message="Role name must contain alphabets only")
    private String name;

    @NotNull
    private String active;

}
