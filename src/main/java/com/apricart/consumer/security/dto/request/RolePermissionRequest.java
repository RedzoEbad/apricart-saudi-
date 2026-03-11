package com.apricart.consumer.security.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class RolePermissionRequest {

    private Long id;

    @NotNull(message= "{ROLE_ID_NOT_NULL}")
    private Long roleId;

    @NotNull(message= "{PERMISSION_ID_NOT_NULL}")
    private List<Long> permissionId;

}
