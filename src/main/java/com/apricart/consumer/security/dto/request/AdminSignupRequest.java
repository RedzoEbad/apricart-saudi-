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
public class AdminSignupRequest {

	@NotEmpty(message = "{registration_email_not_empty}")
	@Email(message = "{registration_email_is_not_valid}")
	private String email;

	@NotEmpty(message = "{registration_password_not_empty}")
	private String password;
}
