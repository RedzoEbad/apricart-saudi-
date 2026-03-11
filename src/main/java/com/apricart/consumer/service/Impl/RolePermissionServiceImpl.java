package com.apricart.consumer.service.Impl;

import com.apricart.consumer.enity.Permission;
import com.apricart.consumer.enity.RolePermissionMapper;
import com.apricart.consumer.enity.Roles;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.repository.jpa.RolePermissionMapperRepository;
import com.apricart.consumer.security.dto.dto.RolesPermissionDTO;
import com.apricart.consumer.security.dto.request.RolePermissionRequest;
import com.apricart.consumer.security.mapper.PermissionMapper;
import com.apricart.consumer.service.PermissionService;
import com.apricart.consumer.service.RolePermissionService;
import com.apricart.consumer.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.apricart.consumer.security.mapper.PermissionMapper.toRolePermissionDto;


@Service
public class RolePermissionServiceImpl implements RolePermissionService {
    @Autowired
    RoleService roleService;
    @Autowired
    PermissionService permissionService;
    @Autowired
    RolePermissionMapperRepository rolePermissonRepository;

    private static final String ROLE_NOT_FOUND = "Role not found";
    private static final String NO_PERMISSION_LINKED = "No permission is linked";


    @Override
    public List<RolePermissionMapper> findPermissionByRole(Long roleId) {
        return (List<RolePermissionMapper>) rolePermissonRepository.findByRolesId(roleId);
    }
    @Override
    public RolePermissionMapper save(RolePermissionMapper rolePermissionMapper) {
        return rolePermissonRepository.save(rolePermissionMapper);
    }
    @Override
    public ResponseEntity<?> getAllPermissionByRoleID(Long roleId) {
        List<RolePermissionMapper> permissions = findPermissionByRole(roleId);
        if(permissions.isEmpty()){
            return Response.error(NO_PERMISSION_LINKED);
        }

        return Response.success(toRolePermissionDto(permissions));
    }

    @Override
    public List<RolesPermissionDTO> getRolesPermissionsByRole(Long roleId) {
        return toRolePermissionDto(findPermissionByRole(roleId));
    }
    @Override
    public  List<RolesPermissionDTO> getPermissions(Long roleId) {
        return toRolePermissionDto(findPermissionByRole(roleId));
    }

    @Override
    public ResponseEntity<?> saveOrUpdateRolePermission(RolePermissionRequest request) {
        try {

            Roles role = roleService.findByIdAndActive(request.getRoleId());
            if(role == null){
                return Response.error(ROLE_NOT_FOUND);
            }
            deleteCurrentPermissions(role.getId());

            for (Long p : request.getPermissionId()) {
                Permission permission = permissionService.findByIdAndActive(p);
                RolePermissionMapper rolesPermission = PermissionMapper.toRolePermission(role, permission);

                if (rolesPermission != null) {
                    save(rolesPermission);
                }
            }
            return Response.success();
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
    }
    @Override
    public void deleteCurrentPermissions(Long roleId) {
        if(roleId != null) {
            rolePermissonRepository.deleteByRolesId(roleId);
        }
    }

}
