package com.apricart.consumer.security.mapper;

import com.apricart.consumer.enity.Permission;
import com.apricart.consumer.enity.RolePermissionMapper;
import com.apricart.consumer.enity.Roles;
import com.apricart.consumer.security.dto.dto.PermissionDTO;
import com.apricart.consumer.security.dto.dto.RolesPermissionDTO;

import java.util.*;

public class PermissionMapper
{
    public static RolePermissionMapper toRolePermission(Roles role, Permission permission) {
        if (role == null || permission == null) {
            return null;
        }
        return RolePermissionMapper.builder().permission(permission).roles(role).build();
    }
    public static List<RolesPermissionDTO> toRolePermissionDto(List<RolePermissionMapper> mapperList) {
        List<RolesPermissionDTO> finalist = new ArrayList<>();
        if (mapperList.size() == 0) {
            return null;
        }

        for (RolePermissionMapper p : mapperList) {
             finalist.add(RolesPermissionDTO.builder().permission(toPermissionDto(p.getPermission())).id(p.getId()).build());
        }
        return finalist;
    }

    public static PermissionDTO toPermissionDto(Permission permission) {
        if (permission == null) {
            return null;
        }
        return PermissionDTO.builder().id(permission.getId()).apiName(permission.getApiName())
                .apiURL(permission.getApiURL()).category(permission.getCategory()).active(permission.getActive()).build();

    }

    public static List<PermissionDTO> toPermissionListDto(List<Permission> permissions) {
        List<PermissionDTO> finalist = new ArrayList<>();
        if (permissions.size() == 0) {
            return null;
        }

        for (Permission p : permissions) {
            finalist.add(toPermissionDto(p));
        }
        return finalist;
    }

}
