package com.apricart.consumer.service.Impl;

import com.apricart.consumer.enity.UserPortal;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.repository.jpa.UserPortalRepository;
import com.apricart.consumer.security.dto.dto.AuthenticatedUserDto;
import com.apricart.consumer.security.dto.request.AdminLoginRequest;
import com.apricart.consumer.security.dto.response.LoginResponse;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.security.enums.UserRole;
import com.apricart.consumer.security.jwt.JwtTokenManager;
import com.apricart.consumer.service.UserPortalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.*;
import static com.apricart.consumer.security.constants.ResponseMessage.*;

/**
 * Created on July, 2026
 *
 * @author Antigravity
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserPortalServiceImpl implements UserPortalService {

	@Autowired
	private UserPortalRepository userPortalRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private JwtTokenManager jwtTokenManager;



	@Override
	public AuthenticatedUserDto findAuthenticatedUserByUsername(String username) {
		UserPortal user = userPortalRepository.findByUsername(username);
		if (user == null) {
			user = userPortalRepository.findByEmail(username);
		}
		if (user == null) {
			return null;
		}
		return AuthenticatedUserDto.builder()
				.name(user.getName())
				.username(user.getUsername())
				.email(user.getEmail())
				.password(user.getPassword())
				.userRole(UserRole.ADMIN)  
				.build();
	}

	@Override
	public ResponseEntity<?> getPortalLoginResponse(AdminLoginRequest loginRequest, LanguageType languageType) throws Exception {
		String email = loginRequest.getEmail();
		UserPortal user = userPortalRepository.findByEmail(email);

		if (user == null) {
			return LanguageType.ARB.equals(languageType) ? Response.error(ACCOUNT_NOT_FOUND_ARABIC) : Response.error(ACCOUNT_NOT_FOUND);
		}

		if (user.getIsActive() != null && !user.getIsActive()) {
			return LanguageType.ARB.equals(languageType) ? Response.error(USER_NOT_ACTIVE_ERROR_MESSAGE_ARABIC) : Response.error(USER_NOT_ACTIVE_ERROR_MESSAGE);
		}

		if (!bCryptPasswordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
			return LanguageType.ARB.equals(languageType) ? Response.error(INVALID_CREDENTIALS_ARABIC) : Response.error(INVALID_CREDENTIALS);
		}

		try {
			String token = jwtTokenManager.generateToken(user.getUsername(), UserRole.ADMIN);

			// Save token
			user.setAccessToken(token);
			userPortalRepository.save(user);

			LoginResponse loginResponse = LoginResponse.builder()
					.token(token)
					.userId(user.getId())
					.name(user.getName())
					.phoneNumber(user.getPhoneNumber())
					.email(user.getEmail())
					.build();

			return Response.success(loginResponse);
		} catch (Exception e) {
			return Response.error(e.getMessage());
		}
	}
}
