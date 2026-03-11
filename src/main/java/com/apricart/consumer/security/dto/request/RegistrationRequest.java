package com.apricart.consumer.security.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.*;

/**
 * Created on August, 2021
 *
 * @author Farrukh Ellahi
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class RegistrationRequest  {

	@NotEmpty()
	private String name;

	private String userName;

	private String arabicName;

	@NotEmpty()
	@Email(message = "Invalid email address format")
	@Pattern(regexp = ".+@.+\\..+", message = "Email must be in a valid format")
	private String email;

	@NotEmpty
	@Size(min = 8, message = "Password must be at least 8 characters long")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$", message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, and one special character")
	private String password;

    @NotNull
	@Pattern(regexp = "\\+?[0-9]+", message = "Invalid phone number format")
	private String phoneNumber;

	@NotNull(message = "City ID is required")
	private Long cityId;

	@NotNull(message = "Salesperson ID is required")
	private Long salesPersonId;

	@NotNull(message = "Trade License is required")
	private String tradelicense;

	@NotNull(message = "Type of Business is required")
	private String typeOfBusiness;
}
