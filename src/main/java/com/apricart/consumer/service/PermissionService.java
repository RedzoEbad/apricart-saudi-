package com.apricart.consumer.service;

import com.apricart.consumer.enity.Permission;
import com.apricart.consumer.security.dto.request.PermissionRequest;
import com.apricart.consumer.security.enums.LanguageType;
import org.springframework.http.ResponseEntity;

public interface PermissionService {

   Permission findById(Long permissionId);

    Permission findByIdAndActive(Long permissionId);

    boolean existsByAPI(String apiUrl);

   Permission save(Permission permission);
   ResponseEntity<?> getAllPermission();

    ResponseEntity<?> getAllCategories();

    ResponseEntity<?> getAllActivePermission();
   ResponseEntity<?> saveOrUpdatePermission(PermissionRequest permissionRequest, LanguageType lang);

}


