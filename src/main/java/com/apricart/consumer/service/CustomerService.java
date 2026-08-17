package com.apricart.consumer.service;

import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.security.dto.dto.AuthenticatedUserDto;
import com.apricart.consumer.security.dto.request.ForgotPasswordRequest;
import com.apricart.consumer.security.dto.request.RegistrationRequest;
import com.apricart.consumer.security.dto.request.UpdatePasswordRequest;
import com.apricart.consumer.security.enums.LanguageType;
import org.springframework.http.ResponseEntity;

/**
 * Created on January, 2024
 *
 * @author Kashaf
 */
public interface CustomerService {

	Customer findById(Long id, LanguageType languageType);
	Customer findActiveCustomerById(Long id, LanguageType lang);
	Customer save(Customer user);
	Customer findByUsername(String username);
	Customer findByPhoneNumber(String phoneNumber);
	void updateAccessToken(Customer user, String accessToken) throws Exception;
	ResponseEntity<?> registration(RegistrationRequest registrationRequest, LanguageType lang);
	ResponseEntity<?> forgotPassword(ForgotPasswordRequest otpVerifyRequest, LanguageType lang);
	ResponseEntity<?> updatePassword(UpdatePasswordRequest updatePasswordRequest, Customer user, LanguageType lang);
	ResponseEntity<?> deleteAccount(Customer user, LanguageType lang);
	AuthenticatedUserDto findAuthenticatedUserByUsername(String username);
}
