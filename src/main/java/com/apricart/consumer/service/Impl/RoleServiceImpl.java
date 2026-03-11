package com.apricart.consumer.service.Impl;

import com.apricart.consumer.enity.Roles;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.repository.jpa.RoleRepository;
import com.apricart.consumer.security.dto.request.RoleRequest;
import com.apricart.consumer.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.apricart.consumer.security.mapper.RoleMapper.toRoleListDto;


@Service
public class RoleServiceImpl implements RoleService {

    public static final String NO_PERMISSION_LINKED = "No Permission linked with provided Role ID";
    public static final String NO_PERMISSION_ROLE_ACTIVE = "Please check the status of permission or role";
    public static final String SUPER_ADMIN_CANNOT_UPDATE = "Super Admin cannot be set as Inactive";
    public static final String ROLE_NOT_FOUND = "No Role Found";

    public static final String YES = "Y";
    public static final String NO = "N";
    @Autowired
    RoleRepository roleRepository;
    @Override
    public Roles findByIdAndActive(Long roleId) {
        return roleRepository.findByIdAndActive(roleId, YES);
    }

    @Override
    public Roles findById(Long roleId) {
        return roleRepository.findById(roleId).orElse(null);
    }
    @Override
    public Roles save(Roles role) {
        return roleRepository.save(role);
    }
    @Override
    public ResponseEntity<?> getAllRoles() {

        List<Roles> roles = (List<Roles>) roleRepository.findAll();
        return Response.success(toRoleListDto(roles, false));
    }
    @Override
    public ResponseEntity<?> getAllActiveRoles() {

        List<Roles> roles = (List<Roles>) roleRepository.findByActive(YES);
        return Response.success(toRoleListDto(roles, false));
    }

    @Override
    public ResponseEntity<?> getAllActiveRolesWithUsers() {

        List<Roles> roles = (List<Roles>) roleRepository.findByActive(YES);
        return Response.success(toRoleListDto(roles, true));
    }

    @Override
    public ResponseEntity<?> getAllRolesWithUsers() {

        List<Roles> roles = (List<Roles>) roleRepository.findAll();
        return Response.success(toRoleListDto(roles, true));
    }
    @Override
    public ResponseEntity<?> saveOrUpdateRole(RoleRequest roleRequest) {
        try {

            if(roleRequest.getId() != null && roleRequest.getId() == 1 && roleRequest.getActive().equalsIgnoreCase(NO)) {
                return Response.error(SUPER_ADMIN_CANNOT_UPDATE);
            }

            Roles role = new Roles();
            if(roleRequest.getId() != null) {
                 role = findById(roleRequest.getId());
            }

            if(roleRequest.getId() != null && role == null){
                return Response.error(ROLE_NOT_FOUND);
            }
            role.setName(roleRequest.getName());
            role.setActive(roleRequest.getActive());
            save(role);
            return Response.success();
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
    }


}
