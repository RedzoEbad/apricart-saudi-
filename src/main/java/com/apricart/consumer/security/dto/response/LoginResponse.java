package com.apricart.consumer.security.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created on January, 2024
 *
 * @author Kashaf
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class LoginResponse {

	private String token;

	private Long userId;

	private String name;

	private String phoneNumber;

	private String email;


}
