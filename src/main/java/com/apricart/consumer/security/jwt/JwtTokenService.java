package com.apricart.consumer.security.jwt;

import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.security.dto.dto.AuthenticatedUserDto;
import com.apricart.consumer.security.dto.dto.OTPRequest;
import com.apricart.consumer.security.dto.request.LoginRequest;
import com.apricart.consumer.security.dto.response.LoginResponse;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.security.mapper.CustomerMapper;
import com.apricart.consumer.service.CustomerService;
import com.apricart.consumer.service.OTPService;
import com.apricart.consumer.utils.Utilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.*;
import static com.apricart.consumer.security.constants.ResponseMessage.*;

/**
 * Created on January, 2024
 *
 * @author Kashaf
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService {
    @Autowired
    CustomerService customerService;

    @Autowired
    OTPService otpService;


    private final JwtTokenManager jwtTokenManager;

    private final AuthenticationManager authenticationManager;

    @Autowired
    private Environment env;

    public ResponseEntity<?> getLoginResponse(LoginRequest loginRequest, LanguageType languageType) throws Exception {

        String phoneNumber = Utilities.cleanSaudiPhoneNumber(loginRequest.getUsername());
        Customer customer = customerService.findByPhoneNumber(phoneNumber);

        if (customer == null) {
            return LanguageType.ARB.equals(languageType) ? Response.error(ACCOUNT_NOT_FOUND_ARABIC) : Response.error(ACCOUNT_NOT_FOUND);
        }

        if (!Boolean.TRUE.equals(customer.getIsActive())) {
            otpService.generateOTP(OTPRequest.builder().phoneNumber(phoneNumber).build(), languageType);
            return LanguageType.ARB.equals(languageType) ? Response.error(USER_INACTIVE_VERIFY_OTP_ARABIC) : Response.error(USER_INACTIVE_VERIFY_OTP);
        }

        try {
            authenticateUser(phoneNumber, loginRequest.getPassword());

            AuthenticatedUserDto authenticatedUserDto = customerService.findAuthenticatedUserByUsername(customer.getPhoneNumber());
            Customer authenticatedUser = CustomerMapper.INSTANCE.convertToUser(authenticatedUserDto);

            String token = jwtTokenManager.generateToken(authenticatedUser);
            updateAuthenticatedUserFields(authenticatedUser, customer, token);
            log.info("{} has successfully logged in!", authenticatedUser.getUsername());

            LoginResponse loginResponse = LoginResponse.builder()
                    .token(token)
                    .userId(customer.getId())
                    .name(customer.getName())
                    .phoneNumber(customer.getPhoneNumber())
                    .email(customer.getEmail())
                    .build();

            return Response.success(loginResponse);

        } catch (AuthenticationException e) {
            return LanguageType.ARB.equals(languageType) ? Response.success(INVALID_CREDENTIALS_ARABIC) : Response.success(INVALID_CREDENTIALS);
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
    }

    private void authenticateUser(String username, String password) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }

    private void updateAuthenticatedUserFields(Customer authenticatedUser, Customer user, String token) throws Exception {
        updateCommonUserFields(authenticatedUser, user);
        authenticatedUser.setAccessToken(token);
        authenticatedUser.setId(customerService.findByUsername(authenticatedUser.getUsername()).getId());
        customerService.updateAccessToken(authenticatedUser, token);
    }


    private void updateCommonUserFields(Customer user, Customer temp) {
        user.setPhoneNumber(temp.getPhoneNumber());
        user.setEmail(temp.getEmail());
        user.setIsActive(true);
        user.setIpAddress(temp.getIpAddress());
        user.setCreateDateTime(temp.getCreateDateTime());
        user.setTradelicense(temp.getTradelicense());
        user.setArabicName(temp.getArabicName());
        user.setTypeOfBusiness(temp.getTypeOfBusiness());
        user.setSalePerson(temp.getSalePerson());
        user.setCity(temp.getCity());
    }


}
