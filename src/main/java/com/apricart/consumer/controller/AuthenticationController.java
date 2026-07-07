package com.apricart.consumer.controller;

import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.security.dto.request.*;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.security.jwt.JwtTokenService;
import com.apricart.consumer.service.BaseService;
import com.apricart.consumer.service.CustomerService;
import com.apricart.consumer.service.UserPortalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created on January, 2024
 *
 * @author Kashaf
 */
@Validated
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
@Api(value = "Authentication Controller", tags = { "Authentication" })
public class AuthenticationController {

	@Autowired
	BaseService baseService;
	@Autowired
	CustomerService customerService;
	@Autowired
	UserPortalService userPortalService;

	private final JwtTokenService jwtTokenService;

	@ApiOperation(value = "Customer Registration")
	@PostMapping("/open/register")
	public ResponseEntity<?> registerCustomer(@Valid @RequestBody RegistrationRequest request,
			@RequestHeader("Language") LanguageType lang) {
		return customerService.registration(request, lang);
	}

	@ApiOperation(value = "Customer Login")
	@PostMapping("/open/login")
	public ResponseEntity<?> loginRequest(@Valid @RequestBody LoginRequest loginRequest,
			@RequestHeader("Language") LanguageType lang) throws Exception {
		return jwtTokenService.getLoginResponse(loginRequest, lang);
	}

	@ApiOperation(value = "Forgot Password")
	@PostMapping("/open/password/forgot")
	public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest,
			@RequestHeader("Language") LanguageType lang) {
		return customerService.forgotPassword(forgotPasswordRequest, lang);
	}

	@ApiOperation(value = "Change Password")
	@PostMapping("/close/password/update")
	public ResponseEntity<?> updatePassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest,
			HttpServletRequest request, @RequestHeader("Language") LanguageType lang) {
		Customer user = baseService.resolveUser(request);
		return customerService.updatePassword(updatePasswordRequest, user, lang);
	}

	@ApiOperation(value = "Admin Login")
	@PostMapping("/open/admin/login")
	public ResponseEntity<?> portalLoginRequest(@Valid @RequestBody AdminLoginRequest loginRequest,
			@RequestHeader("Language") LanguageType lang) throws Exception {
		return userPortalService.getPortalLoginResponse(loginRequest, lang);
	}

	@ApiOperation(value = "Admin Signup")
	@PostMapping("/open/admin/signup")
	public ResponseEntity<?> portalSignupRequest(@Valid @RequestBody AdminSignupRequest signupRequest,
			@RequestHeader("Language") LanguageType lang) throws Exception {
		return userPortalService.getPortalSignupResponse(signupRequest, lang);
	}

	@ApiOperation(value = "Admin OTP Verification")
	@PostMapping("/open/admin/verify")
	public ResponseEntity<?> portalVerifyRequest(@Valid @RequestBody OtpVerifyEmailRequest verifyRequest,
			@RequestHeader("Language") LanguageType lang) throws Exception {
		return userPortalService.getPortalVerifyResponse(verifyRequest, lang);
	}
}
