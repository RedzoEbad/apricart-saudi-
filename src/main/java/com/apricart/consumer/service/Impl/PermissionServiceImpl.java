package com.apricart.consumer.service.Impl;

import com.apricart.consumer.enity.Permission;
import com.apricart.consumer.exceptions.NotFoundException;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.repository.jpa.PermissionRepository;
import com.apricart.consumer.security.dto.request.PermissionRequest;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.EXISTS_BY_API_URL_ARABIC;
import static com.apricart.consumer.security.constants.ResponseMessage.EXISTS_BY_API_URL;
import static com.apricart.consumer.security.mapper.PermissionMapper.toPermissionListDto;

@Service
public class PermissionServiceImpl implements PermissionService {

    public static final String YES = "Y";
    public static final String NO = "N";

    @Autowired
    PermissionRepository permissionRepository;

    @Override
    public Permission findById(Long permissionId) {
        return permissionRepository.findById(permissionId).orElseThrow(() -> new NotFoundException(Permission.class.getName(),permissionId));
    }
    @Override
    public Permission findByIdAndActive(Long permissionId) {
        return permissionRepository.findByIdAndActive(permissionId, YES);
    }
    @Override
    public boolean existsByAPI(String apiUrl) {
        return permissionRepository.existsByApiURL(apiUrl);
    }
    @Override
    public Permission save(Permission permission) {
        return permissionRepository.save(permission);
    }
    public ResponseEntity<?> getAllPermission() {

        List<Permission> permissions = (List<Permission>) permissionRepository.findAll();
        return Response.success(toPermissionListDto(permissions));
    }
    @Override
    public ResponseEntity<?> getAllCategories() {

        List<String> permissions = permissionRepository.findDistinctCategory();
        return Response.success(permissions);
    }

    @Override
    public ResponseEntity<?> getAllActivePermission() {

        List<Permission> permissions = (List<Permission>) permissionRepository.findByActive(YES);
        return Response.success(toPermissionListDto(permissions));
    }

    @Override
    public ResponseEntity<?> saveOrUpdatePermission(PermissionRequest permissionRequest, LanguageType lang) {
        try {
            Permission permission = new Permission();
            if(permissionRequest.getId() != null) {
                 permission = findById(permissionRequest.getId());
            }
            if(permission.getId() == null && existsByAPI(permissionRequest.getApiURL())){
                return Response.error(LanguageType.ARB.equals(lang) ? EXISTS_BY_API_URL_ARABIC : EXISTS_BY_API_URL);
            }
            permission.setApiName(permissionRequest.getApiName());
            permission.setApiURL(permissionRequest.getApiURL());
            permission.setActive(permissionRequest.getActive());
            permission.setCategory(permissionRequest.getCategory());
            permission.setRolePermissionMapper(null);

            return Response.success(save(permission));
        } catch (Exception e) {
            return Response.error(e.getMessage());

        }
    }


}
