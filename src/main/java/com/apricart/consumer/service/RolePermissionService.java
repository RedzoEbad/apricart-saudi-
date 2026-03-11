package com.apricart.consumer.service;

import com.apricart.consumer.enity.RolePermissionMapper;
import com.apricart.consumer.security.dto.dto.RolesPermissionDTO;
import com.apricart.consumer.security.dto.request.RolePermissionRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RolePermissionService {

   List<RolePermissionMapper> findPermissionByRole(Long roleId);

    RolePermissionMapper save(RolePermissionMapper rolePermissionMapper);
    ResponseEntity<?> getAllPermissionByRoleID(Long roleId);
    List<RolesPermissionDTO> getRolesPermissionsByRole(Long roleId);
    List<RolesPermissionDTO> getPermissions(Long roleId);
    ResponseEntity<?> saveOrUpdateRolePermission(RolePermissionRequest rolePermissionRequest);
   void deleteCurrentPermissions(Long roleId);
}


