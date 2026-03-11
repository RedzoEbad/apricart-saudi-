package com.apricart.consumer.service.Impl;


import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.enity.Otp;
import com.apricart.consumer.exceptions.NotFoundException;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.repository.jpa.OtpRepository;
import com.apricart.consumer.security.dto.dto.OTPRequest;
import com.apricart.consumer.security.dto.request.OtpVerifyRequest;
import com.apricart.consumer.security.dto.response.LoginResponse;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.security.jwt.JwtTokenManager;
import com.apricart.consumer.service.CustomerService;
import com.apricart.consumer.service.OTPService;
import com.apricart.consumer.utils.EmailUtils;
import com.apricart.consumer.utils.Utilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.*;
import static com.apricart.consumer.security.constants.Constants.EMAIL_SUBJECT_OTP;
import static com.apricart.consumer.security.constants.ResponseMessage.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class OTPServiceImpl implements OTPService {
    protected static final Logger LOGGER = LoggerFactory.getLogger(OTPServiceImpl.class);

    @Autowired
    CustomerService customerService;
    @Autowired
    JwtTokenManager jwtTokenManager;
    @Autowired
    OtpRepository otpRepository;
    @Autowired
    EmailUtils emailUtils;

    private static final long OTP_EXPIRY_DURATION_SECONDS = 60L;

    @Override
    public ResponseEntity<?> generateOTP(@Valid OTPRequest otpRequest, LanguageType languageType) throws Exception {
        try {

            String response = sendViaSMSAndEmail(otpRequest.getPhoneNumber(), languageType);
            String msg = LanguageType.ARB.equals(languageType) ? OTP_SENT_SUCCESSFULLY_ARABIC : OTP_SENT_SUCCESSFULLY;

            return response.equalsIgnoreCase(msg) ? Response.created(response) : Response.error(response);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }


    @Override
    public ResponseEntity<?> verify(OtpVerifyRequest otpVerifyRequest, LanguageType lang) {
        String phoneNumber = Utilities.cleanSaudiPhoneNumber(otpVerifyRequest.getPhoneNumber());
        try {
            Otp otp = otpRepository.findByPhoneNumber(phoneNumber).orElse(null);

            if (otp.getOtp() != null && otp.getOtp().equalsIgnoreCase(otpVerifyRequest.getOtp())) {
                log.info("{} verified successfully!", phoneNumber);

                Customer customer = customerService.findByPhoneNumber(phoneNumber);
                if (customer == null) {
                    return Response.error(LanguageType.ARB.equals(lang) ? ACCOUNT_NOT_FOUND_ARABIC : ACCOUNT_NOT_FOUND);
                }
                customer.setUpdateDateTime(LocalDateTime.now());
                customer.setIsActive(true);
                customerService.save(customer);


                otp.setOtp("");
                save(otp);

                final String token = jwtTokenManager.generateToken(customer);
                customerService.updateAccessToken(customer, token);

                LoginResponse loginResponse = LoginResponse.builder()
                        .token(token)
                        .userId(customer.getId())
                        .name(customer.getName())
                        .phoneNumber(customer.getPhoneNumber())
                        .email(customer.getEmail())
                        .build();
                return Response.success(LanguageType.ARB.equals(lang) ? OTP_VERIFIED_SUCCESSFULLY_ARABIC : OTP_VERIFIED_SUCCESSFULLY, loginResponse);
            } else
                return Response.error(LanguageType.ARB.equals(lang) ? OTP_INVALID_ARABIC : OTP_INVALID);

        } catch (Exception e) {
            log.error("Exception occurred during OTP verification: {}", e.getMessage());
            return Response.error(LanguageType.ARB.equals(lang) ? ERROR_FAILED_ARABIC : ERROR_FAILED + e.getMessage());
        }
    }


    @Override
    public Otp save(Otp otp) {
        return otpRepository.save(otp);
    }

    @Override
    public String findOTPByPhoneNumber(String phoneNumber) {

        Optional<Otp> otp = otpRepository.findByPhoneNumber(phoneNumber);
        return otp.map(Otp::getOtp).orElse(null);
    }

    @Override
    public Otp findByPhoneNumber(String phoneNumber) {
        return otpRepository.findByPhoneNumber(phoneNumber).orElseGet(Otp::new);
    }

    @Override
    public void sendOTPEmail(Customer user) throws Exception {
            EmailUtils.SUBJECT = EMAIL_SUBJECT_OTP;
            emailUtils.sendOTPEmail(user.getEmail(), user.getName(), findOTPByPhoneNumber(user.getPhoneNumber()));
    }

    public int findLastEntryId() {
        return Math.toIntExact(otpRepository.findTopByOrderByIdDesc()
                .map(Otp::getId)
                .orElseThrow(() -> new NotFoundException("No OTP entries found.")));
    }


    private String sendViaSMSAndEmail(String phoneNumber, LanguageType lang) throws Exception {
        String generatedCode = generateOTPCode();
        Otp otp = otpRepository.findByPhoneNumber(phoneNumber).orElseGet(Otp::new);

        if (isOtpRecentlyUpdated(otp)) {
            long remainingSeconds = getRemainingSeconds(otp);
            String msg = getLocalizedMessage(lang, TRY_AFTER_SECONDS, TRY_AFTER_SECONDS_ARABIC);
            return String.format(msg, remainingSeconds);
        }

        if (sendOneTimePassword(generatedCode, phoneNumber)) {
            updateOtp(otp, generatedCode, phoneNumber);
            Customer customer = customerService.findByPhoneNumber(phoneNumber);

            if (customer != null) {
                sendOTPEmail(customer);
                return getLocalizedMessage(lang, OTP_SENT_SUCCESSFULLY, OTP_SENT_SUCCESSFULLY_ARABIC);
            } else {
                return getLocalizedMessage(lang, ACCOUNT_NOT_FOUND, ACCOUNT_NOT_FOUND_ARABIC);
            }
        } else {
            return getLocalizedMessage(lang, OTP_SEND_FAILURE, OTP_SEND_FAILURE_ARABIC);
        }
    }

    private boolean isOtpRecentlyUpdated(Otp otp) {
        return otp.getUpdateDateTime() != null && ChronoUnit.SECONDS.between(otp.getUpdateDateTime(), LocalDateTime.now()) <= OTP_EXPIRY_DURATION_SECONDS;
    }

    private long getRemainingSeconds(Otp otp) {
        long seconds = ChronoUnit.SECONDS.between(otp.getUpdateDateTime(), LocalDateTime.now());
        return OTP_EXPIRY_DURATION_SECONDS - seconds;
    }

    private String getLocalizedMessage(LanguageType lang, String defaultMsg, String arabicMsg) {
        return LanguageType.ARB.equals(lang) ? arabicMsg : defaultMsg;
    }

    private void updateOtp(Otp otp, String generatedCode, String phoneNumber) {
        otp.setOtp(generatedCode);
        otp.setPhoneNumber(phoneNumber);
        save(otp);
    }

    public static boolean sendOneTimePassword(String otp, String phoneNumber) {
        try {
            LOGGER.info("Current IP address: {} Current Hostname: {}", InetAddress.getLocalHost(), InetAddress.getLocalHost().getHostName());

//            String response = Utilities.getObject("", false, ""); // handle if response is error later once service is decided
//            LOGGER.info("SMS API response: " + response);

            // Log for development testing
            LOGGER.info("Development testing: OTP sent successfully to " + phoneNumber + " with code: " + otp);

            return true;
        } catch (Exception e) {
            LOGGER.error("Error while sending OTP message: " + e.getMessage(), e);
            return false;
        }
    }


    private String generateOTPCode() {
        return RandomStringUtils.randomNumeric(4);
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null &&
                ((startsWithPrefix(phoneNumber, "+923") ||
                        startsWithPrefix(phoneNumber, "923") ||
                        startsWithPrefix(phoneNumber, "03") ||
                        startsWithPrefix(phoneNumber, "3")) &&
                        phoneNumber.length() <= 10);
    }

    private static boolean startsWithPrefix(String phoneNumber, String prefix) {
        return phoneNumber.startsWith(prefix);
    }
}