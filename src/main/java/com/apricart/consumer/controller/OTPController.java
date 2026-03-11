package com.apricart.consumer.controller;

import com.apricart.consumer.enity.Otp;
import com.apricart.consumer.generic.GenericResponse;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.security.dto.dto.OTPRequest;
import com.apricart.consumer.security.dto.request.OtpVerifyRequest;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.OTPService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.OTP_RETRIEVED_SUCCESSFULLY_ARABIC;
import static com.apricart.consumer.security.constants.ResponseMessage.OTP_RETRIEVED_SUCCESSFULLY;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth/open/otp")
@Api(value = "OTP Controller", tags = {"OTP"})
public class OTPController {
    @Autowired
    OTPService otpService;

    @ApiOperation(value = "Generate One-Time Password (OTP)")
    @PostMapping("/generate")
    public ResponseEntity<?> generateOTP(@Valid @RequestBody OTPRequest otpRequest, @RequestHeader("Language") LanguageType lang) throws Exception {
        return otpService.generateOTP(otpRequest, lang);
    }

    @ApiOperation(value = "Verify One-Time Password (OTP) via SMS")
    @PostMapping("/verify")
    public ResponseEntity<?> verifyOTPViaSMS(@Valid @RequestBody OtpVerifyRequest otpVerifyRequest, @RequestHeader("Language") LanguageType lang) {
        return otpService.verify(otpVerifyRequest, lang);
    }

    @ApiOperation(value = "Generate One-Time Password (OTP)")
    @GetMapping("/getOTPCode")
    public ResponseEntity<GenericResponse<String>> findByPhoneNumber(@RequestParam String phoneNumber, @RequestHeader("Language") LanguageType lang) throws Exception {
        Otp otp = otpService.findByPhoneNumber(phoneNumber);
        return  otp.getOtp() != null && !otp.getOtp().isEmpty() ? Response.success(LanguageType.ARB.equals(lang) ? OTP_RETRIEVED_SUCCESSFULLY_ARABIC : OTP_RETRIEVED_SUCCESSFULLY, otp.getOtp()) : Response.notFound();
    }
}