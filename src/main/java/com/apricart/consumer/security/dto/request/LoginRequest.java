package com.apricart.consumer.security.dto.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;

/**
 * Created on January, 2024
 *
 * @author Kashaf
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest  {

	@NotEmpty()
	private String username;

	@NotEmpty()
	private String password;

}
