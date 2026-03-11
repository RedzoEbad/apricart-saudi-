package com.apricart.consumer.security.dto.dto;

import com.apricart.consumer.security.enums.UserRole;
import lombok.*;


/**
 * Created on August, 2021
 *
 * @author Farrukh Ellahi
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticatedUserDto {

	private String name;

	private String username;

	private String email;

	private String password;

	private UserRole userRole;

}
