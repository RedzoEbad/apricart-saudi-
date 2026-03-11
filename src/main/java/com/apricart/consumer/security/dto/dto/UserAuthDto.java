package com.apricart.consumer.security.dto.dto;

import com.apricart.consumer.security.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAuthDto {
    private Long id;

    private String name;

    private String email;

    private String phoneNumber;

    private Boolean isActive = true;

    private UserRole userRole;

    private String type;

    private String apis;

}
