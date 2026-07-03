package com.apricart.consumer.service.Impl;

import com.apricart.consumer.security.enums.UserRole;
import com.apricart.consumer.security.dto.dto.AuthenticatedUserDto;
import com.apricart.consumer.service.CustomerService;
import com.apricart.consumer.service.UserPortalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;

/**
 * Created on August, 2021
 *
 * @author Farrukh Ellahi
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private static final String USERNAME_OR_PASSWORD_INVALID = "Invalid username or password.";

	private final CustomerService customerService;
	private final UserPortalService userPortalService;

	@Override
	public UserDetails loadUserByUsername(String username) {

		AuthenticatedUserDto authenticatedUser = null;
		try {
			authenticatedUser = customerService.findAuthenticatedUserByUsername(username);
		} catch (Exception e) {
			log.debug("User not found in Customer service for username: {}", username);
		}

		if (Objects.isNull(authenticatedUser)) {
			try {
				authenticatedUser = userPortalService.findAuthenticatedUserByUsername(username);
			} catch (Exception e) {
				log.debug("User not found in UserPortal service for username: {}", username);
			}
		}

		if (Objects.isNull(authenticatedUser)) {
			throw new UsernameNotFoundException(USERNAME_OR_PASSWORD_INVALID);
		}

		final String authenticatedUsername = authenticatedUser.getUsername();
		final String authenticatedPassword = authenticatedUser.getPassword();
		final UserRole userRole = authenticatedUser.getUserRole();
		final SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(userRole.name());

		return new User(authenticatedUsername, authenticatedPassword, Collections.singletonList(grantedAuthority));
	}
}
