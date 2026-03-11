package com.apricart.consumer.service;

import com.apricart.consumer.enity.Roles;
import com.apricart.consumer.security.dto.request.RoleRequest;
import org.springframework.http.ResponseEntity;

public interface RoleService {

   Roles findByIdAndActive(Long roleId);

    Roles findById(Long roleId);

    Roles save(Roles role);
   ResponseEntity<?> getAllRoles();
   ResponseEntity<?> getAllActiveRoles();

   ResponseEntity<?> getAllActiveRolesWithUsers();

    ResponseEntity<?> getAllRolesWithUsers();

    ResponseEntity<?> saveOrUpdateRole(RoleRequest roleRequest);

}


