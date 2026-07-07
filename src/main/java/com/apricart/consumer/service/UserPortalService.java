package com.apricart.consumer.service;

import com.apricart.consumer.security.dto.dto.AuthenticatedUserDto;
import com.apricart.consumer.security.dto.request.AdminLoginRequest;
import com.apricart.consumer.security.dto.request.AdminSignupRequest;
import com.apricart.consumer.security.dto.request.OtpVerifyEmailRequest;
import com.apricart.consumer.security.enums.LanguageType;
import org.springframework.http.ResponseEntity;

/**
 * Created on July, 2026
 *
 * @author Kashaf
 */
public interface UserPortalService {

	AuthenticatedUserDto findAuthenticatedUserByUsername(String username);

	ResponseEntity<?> getPortalLoginResponse(AdminLoginRequest loginRequest, LanguageType languageType) throws Exception;

	ResponseEntity<?> getPortalSignupResponse(AdminSignupRequest signupRequest, LanguageType languageType) throws Exception;

	ResponseEntity<?> getPortalVerifyResponse(OtpVerifyEmailRequest verifyRequest, LanguageType languageType) throws Exception;
}
