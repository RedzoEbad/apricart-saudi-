package com.apricart.consumer.service;

import com.apricart.consumer.enity.Roles;
import com.apricart.consumer.exceptions.RegistrationException;
import com.apricart.consumer.repository.jpa.CustomerRepository;
import com.apricart.consumer.repository.jpa.RoleRepository;
import com.apricart.consumer.security.dto.request.RegistrationRequest;
import com.apricart.consumer.utils.ExceptionMessageAccessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created on August, 2021
 *
 * @author Farrukh Ellahi
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserValidationService {

	private static final String EMAIL_ALREADY_EXISTS = "email_already_exists";

	private static final String USERNAME_ALREADY_EXISTS = "username_already_exists";
	private static final String ROLE_NOT_FOUND = "Role not found";


	private final CustomerRepository customerRepository;
	private final RoleRepository roleRepository;
	private final ExceptionMessageAccessor exceptionMessageAccessor;

	public void validateUser(RegistrationRequest registrationRequest) {

		final String email = registrationRequest.getEmail();
		final String username = registrationRequest.getUserName();

		checkEmail(email);
		checkUsername(username);
	}

	private void checkUsername(String username) {

		final boolean existsByUsername = customerRepository.existsByUsername(username);

		if (existsByUsername) {

			log.warn("{} is already being used!", username);

			final String existsUsername = exceptionMessageAccessor.getMessage(null, USERNAME_ALREADY_EXISTS);
			throw new RegistrationException(existsUsername);
		}

	}

	private void checkEmail(String email) {

		final boolean existsByEmail = customerRepository.existsByEmail(email);

		if (existsByEmail) {

			log.warn("{} is already being used!", email);

			final String existsEmail = exceptionMessageAccessor.getMessage(null, EMAIL_ALREADY_EXISTS);
			throw new RegistrationException(existsEmail);
		}
	}

	public Roles getRoleById(Long roleId) {

		Roles role = roleRepository.findById(roleId).orElse(null);
		if(role == null){
			throw new RegistrationException(exceptionMessageAccessor.getMessage(null, ROLE_NOT_FOUND));
		}
		else return role;

	}
}
