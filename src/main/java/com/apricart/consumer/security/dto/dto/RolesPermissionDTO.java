package com.apricart.consumer.security.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolesPermissionDTO {

    private Long id;
    private PermissionDTO permission;
//    private RolesDTO roles;

}
