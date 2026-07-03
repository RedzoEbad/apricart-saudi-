package com.apricart.consumer.security.dto.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * Created on July, 2026
 *
 * @author Antigravity
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminLoginRequest {

	@NotEmpty()
	@Email()
	private String email;

	@NotEmpty()
	private String password;
}
