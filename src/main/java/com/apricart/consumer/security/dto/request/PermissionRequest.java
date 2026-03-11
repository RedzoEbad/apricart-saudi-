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
public class PermissionRequest {

    private Long id;

    @NotNull(message= "{PERMISSION_API_NAME_NOT_NULL}")
    @Pattern(regexp="^[a-zA-Z0-9 ]*$",message="Permission name must contain alphabets only")
    private String apiName;

    @NotNull(message="{PERMISSION_API_URL_NOT_NULL}")
    @Pattern(regexp="^/[a-zA-Z0-9/]+$",message="Invalid URL format")
    private String apiURL;

    @NotNull
    private String active;
    private String category;

}
