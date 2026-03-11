package com.apricart.consumer.service.Impl;

import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.enity.Roles;
import com.apricart.consumer.enity.UserPortal;
import com.apricart.consumer.repository.jpa.CustomerRepository;
import com.apricart.consumer.security.dto.dto.RolesPermissionDTO;
import com.apricart.consumer.security.enums.EnvironmentType;
import com.apricart.consumer.service.BaseService;
import com.apricart.consumer.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Service
public class BaseServiceImpl implements BaseService {
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    RolePermissionService rolePermissionService;
    public static final int SUPER_ADMIN = 1;

    @Override
    public Customer resolveUser(HttpServletRequest request) {
        try {
            Customer user = customerRepository.findByPhoneNumber(request.getRemoteUser());
            String user_agent = "", ip_address = "", remoteHost = "", remoteAddr = "";

            try {
                user_agent = request.getHeader("User-Agent").trim();
                remoteHost = request.getRemoteHost(); remoteAddr= request.getRemoteAddr();
                if (!remoteHost.isEmpty()) { ip_address = remoteHost; }
                if (!remoteAddr.isEmpty()) { ip_address = remoteAddr; }
            }
            catch (Exception ignored) { }
            return user;
        } catch (Exception e) {
            return new Customer();
        }
    }
    @Override
    public String isEmpty(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        return value;
    }
    @Override
    public String isEmpty() {
        return "";
    }
    @Override
    public boolean isEmptyCheck(String value) {
        return value == null || value.isEmpty();
    }
    @Override
    public boolean isNotLocal(Environment env) {
        String baseUrl = Objects.requireNonNull(env.getProperty("server.consumer.baseurl")).toLowerCase();
        return baseUrl.contains(EnvironmentType.PRODUCTION.getUrl()) || baseUrl.contains(EnvironmentType.STAGING.getUrl());
    }

    @Override
    public boolean getAuthorization(Roles roles, String endPoint) {
        if(roles != null) {
            List<RolesPermissionDTO> permissions = rolePermissionService.getRolesPermissionsByRole(roles.getId());
            return permissions.stream().anyMatch(s -> s.getPermission().getApiURL().equalsIgnoreCase(endPoint));
        }
        return false;
    }

    @Override
    public boolean isSuperAdmin(UserPortal user) {
        if(user.getRoles() != null) {
            return user.getRoles().getId() == SUPER_ADMIN;
        }
        return false;
    }
}
