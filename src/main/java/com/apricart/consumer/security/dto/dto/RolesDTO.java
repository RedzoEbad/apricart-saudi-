package com.apricart.consumer.security.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolesDTO {

    private Long id;
    private String name;
    private String active;
    private List<UserDto> users;

}
