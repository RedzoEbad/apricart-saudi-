package com.apricart.consumer.service.Impl;

import com.apricart.consumer.enity.Otp;
import com.apricart.consumer.enity.Roles;
import com.apricart.consumer.enity.UserPortal;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.repository.jpa.OtpRepository;
import com.apricart.consumer.repository.jpa.RoleRepository;
import com.apricart.consumer.repository.jpa.UserPortalRepository;
import com.apricart.consumer.security.dto.dto.AuthenticatedUserDto;
import com.apricart.consumer.security.dto.request.AdminLoginRequest;
import com.apricart.consumer.security.dto.request.AdminSignupRequest;
import com.apricart.consumer.security.dto.request.OtpVerifyEmailRequest;
import com.apricart.consumer.security.dto.response.LoginResponse;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.security.enums.UserRole;
import com.apricart.consumer.security.jwt.JwtTokenManager;
import com.apricart.consumer.service.UserPortalService;
import com.apricart.consumer.utils.EmailUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
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

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private OtpRepository otpRepository;

	@Autowired
	private EmailUtils emailUtils;

	private static final String EMAIL_EXISTS = "Email is already registered!";
	private static final String EMAIL_EXISTS_ARABIC = "البريد الإلكتروني مسجل بالفعل!";



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

	@Override
	public ResponseEntity<?> getPortalSignupResponse(AdminSignupRequest signupRequest, LanguageType languageType) throws Exception {
		try {
			String email = signupRequest.getEmail();
			UserPortal user = userPortalRepository.findByEmail(email);

			if (user != null && Boolean.TRUE.equals(user.getIsActive())) {
				return languageType.equals(LanguageType.ARB) ? Response.error(EMAIL_EXISTS_ARABIC) : Response.error(EMAIL_EXISTS);
			}

			if (user == null) {
				Roles adminRole = roleRepository.findById(1L).orElse(null);
				user = UserPortal.builder()
						.name("Admin User")
						.username(email)
						.email(email)
						.password(bCryptPasswordEncoder.encode(signupRequest.getPassword()))
						.isActive(false)
						.roles(adminRole)
						.build();
				userPortalRepository.save(user);
			} else {
				user.setPassword(bCryptPasswordEncoder.encode(signupRequest.getPassword()));
				userPortalRepository.save(user);
			}

			// Generate OTP
			String otpCode = RandomStringUtils.randomNumeric(4);

			// Save to Otp entity (we save it mapping the email to phoneNumber column)
			Otp otp = otpRepository.findByPhoneNumber(email).orElse(null);
			if (otp == null) {
				otp = Otp.builder()
						.phoneNumber(email)
						.otp(otpCode)
						.build();
			} else {
				otp.setOtp(otpCode);
			}
			otpRepository.save(otp);

			// Send Email
			try {
				EmailUtils.SUBJECT = "Admin Signup Verification Code";
				emailUtils.sendOTPEmail(email, user.getName(), otpCode);
			} catch (Exception mailException) {
				log.warn("Failed to send OTP email to {}: {}", email, mailException.getMessage());
			}
			log.info("Local Dev: Verification OTP code for email {} is {}", email, otpCode);

			String msg = languageType.equals(LanguageType.ARB) ? OTP_SENT_SUCCESSFULLY_ARABIC : OTP_SENT_SUCCESSFULLY;
			return Response.success(msg);

		} catch (Exception e) {
			log.error("Exception during portal signup: {}", e.getMessage(), e);
			return Response.error(e.getMessage());
		}
	}

	@Override
	public ResponseEntity<?> getPortalVerifyResponse(OtpVerifyEmailRequest verifyRequest, LanguageType languageType) throws Exception {
		try {
			String email = verifyRequest.getEmail();
			Otp otp = otpRepository.findByPhoneNumber(email).orElse(null);

			if (otp == null || otp.getOtp() == null || !otp.getOtp().equalsIgnoreCase(verifyRequest.getOtp())) {
				return Response.error(languageType.equals(LanguageType.ARB) ? OTP_INVALID_ARABIC : OTP_INVALID);
			}

			UserPortal user = userPortalRepository.findByEmail(email);
			if (user == null) {
				return Response.error(languageType.equals(LanguageType.ARB) ? ACCOUNT_NOT_FOUND_ARABIC : ACCOUNT_NOT_FOUND);
			}

			user.setIsActive(true);
			String token = jwtTokenManager.generateToken(user.getUsername(), UserRole.ADMIN);
			user.setAccessToken(token);
			userPortalRepository.save(user);

			// Clear OTP code
			otp.setOtp("");
			otpRepository.save(otp);

			LoginResponse loginResponse = LoginResponse.builder()
					.token(token)
					.userId(user.getId())
					.name(user.getName())
					.phoneNumber(user.getPhoneNumber())
					.email(user.getEmail())
					.build();

			return Response.success(languageType.equals(LanguageType.ARB) ? OTP_VERIFIED_SUCCESSFULLY_ARABIC : OTP_VERIFIED_SUCCESSFULLY, loginResponse);

		} catch (Exception e) {
			log.error("Exception during portal verification: {}", e.getMessage(), e);
			return Response.error(e.getMessage());
		}
	}
}
