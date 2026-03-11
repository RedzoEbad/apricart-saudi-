package com.apricart.consumer.service.Impl;

import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.enity.Otp;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.repository.jpa.CustomerRepository;
import com.apricart.consumer.security.dto.dto.OTPRequest;
import com.apricart.consumer.security.dto.request.UpdateProfileRequest;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.CustomerService;
import com.apricart.consumer.service.OTPService;
import com.apricart.consumer.service.ProfileService;
import com.apricart.consumer.utils.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.*;
import static com.apricart.consumer.security.constants.ResponseMessage.*;

@Service
public class ProfileServiceImpl implements ProfileService {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ProfileServiceImpl.class);

    @Autowired
    CustomerService customerService;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    OTPService otpService;

    @Override
    public Customer findCustomerProfile(Customer customer, LanguageType lang) {
        return customerService.findById(customer.getId(), lang);
    }
    @Override
    public ResponseEntity<?> updateProfile(UpdateProfileRequest profileRequest, Customer customer, LanguageType lang) {
        String phoneNumber;
        if (!Boolean.TRUE.equals(customer.getIsActive())) {
            return lang.equals(LanguageType.ARB) ? Response.error(USER_NOT_ACTIVE_ERROR_MESSAGE_ARABIC) : Response.error(USER_NOT_ACTIVE_ERROR_MESSAGE);
        }

        try {
            customer = customerService.findActiveCustomerById(customer.getId(), lang);
            phoneNumber = profileRequest.getPhoneNumber();

            customer.setName(profileRequest.getName() != null ? profileRequest.getName() : customer.getName());
            customer.setEmail(profileRequest.getEmail() != null ? profileRequest.getEmail() : customer.getEmail());
            customer.setUpdateDateTime(LocalDateTime.now());
            customer.setIsActive(true);

            if (phoneNumber != null) {
                if (!Utilities.isValidSaudiPhoneNumber(phoneNumber)) {
                    return lang.equals(LanguageType.ARB) ? Response.error(INVALID_PHONE_NUMBER_ERROR_ARABIC) : Response.error(INVALID_PHONE_NUMBER_ERROR);
                }

                phoneNumber = Utilities.cleanSaudiPhoneNumber(phoneNumber);
                if (customerRepository.existsByPhoneNumber(phoneNumber)) {
                    return lang.equals(LanguageType.ARB) ? Response.error(USER_EXISTS_ERROR_ARABIC) : Response.error(USER_EXISTS_ERROR);
                }

                Otp otp = otpService.findByPhoneNumber(customer.getPhoneNumber());
                otp.setOtp("");
                otpService.save(otp);
                otpService.generateOTP(OTPRequest.builder().phoneNumber(phoneNumber).build(), lang);

                customer.setPhoneNumber(phoneNumber);
                customer.setUsername(phoneNumber);
            }

            customerService.save(customer);

            return lang.equals(LanguageType.ARB) ? Response.success(PROFILE_UPDATE_SUCCESS_MESSAGE_ARABIC) : Response.success(PROFILE_UPDATE_SUCCESS_MESSAGE);
        } catch (Exception e) {
            LOGGER.error("Exception occurred during profile update", e);
            return lang.equals(LanguageType.ARB) ? Response.error(PROFILE_UPDATE_FAILURE_MESSAGE_ARABIC) : Response.error(PROFILE_UPDATE_FAILURE_MESSAGE);
        }
    }
}
