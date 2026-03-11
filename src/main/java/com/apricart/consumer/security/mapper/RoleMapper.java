package com.apricart.consumer.security.mapper;

import com.apricart.consumer.enity.Roles;
import com.apricart.consumer.enity.UserPortal;
import com.apricart.consumer.security.dto.dto.RolesDTO;
import com.apricart.consumer.security.dto.dto.UserDto;
import java.util.ArrayList;
import java.util.List;

public class RoleMapper
{

    public static RolesDTO toRoleDto(Roles role, boolean isUserAllow) {
        if (role == null) {
            return null;
        }
        if(isUserAllow) {
            return RolesDTO.builder().id(role.getId()).active(role.getActive()).name(role.getName()).users(toUsersRoles(role.getUsers())).build();
        }
        else return RolesDTO.builder().id(role.getId()).active(role.getActive()).name(role.getName()).users(null).build();
    }


    public static List<UserDto> toUsersRoles(List<UserPortal> user) {
        List<UserDto> finalist = new ArrayList<>();
        if (user.size() == 0) {
            return null;
        }

        for (UserPortal u : user) {
            finalist.add(toUserDto(u));
        }
        return finalist;
    }
    public static UserDto toUserDto(UserPortal user) {
        if (user == null) {
            return null;
        }

        return UserDto.builder()
                .isActive(user.getIsActive())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .name(user.getName())
                .build();
    }


    public static List<RolesDTO> toRoleListDto(List<Roles> roles, boolean isUserAllow) {
        List<RolesDTO> finalist = new ArrayList<>();
        if (roles.size() == 0) {
            return null;
        }

        for (Roles r : roles) {
            finalist.add(toRoleDto(r, isUserAllow));
        }
        return finalist;
    }

}
