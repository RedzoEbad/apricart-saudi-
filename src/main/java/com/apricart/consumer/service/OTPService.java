package com.apricart.consumer.service;

import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.enity.Otp;
import com.apricart.consumer.security.dto.dto.OTPRequest;
import com.apricart.consumer.security.dto.request.OtpVerifyRequest;
import com.apricart.consumer.security.enums.LanguageType;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;

/**
 * Created on August, 2021
 *
 * @author Farrukh Ellahi
 */
public interface OTPService {


    Otp save(Otp otp);

    String findOTPByPhoneNumber(String phoneNumber);

    Otp findByPhoneNumber(String phoneNumber);
   void sendOTPEmail(Customer user) throws Exception;

    ResponseEntity<?> generateOTP(@Valid OTPRequest otpRequest, LanguageType languageType) throws Exception;

    ResponseEntity<?> verify(OtpVerifyRequest otpVerifyRequest, LanguageType languageType);
}