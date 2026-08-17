package com.apricart.consumer.service.Impl;

import com.apricart.consumer.enity.City;
import com.apricart.consumer.enity.CouponDetail;
import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.enity.CustomerAddress;
import com.apricart.consumer.enity.FeedBack;
import com.apricart.consumer.enity.Otp;
import com.apricart.consumer.exceptions.RegistrationException;
import com.apricart.consumer.exceptions.ResourceNotFoundException;
import com.apricart.consumer.generic.GenericResponse;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.repository.jpa.CartRepository;
import com.apricart.consumer.repository.jpa.CouponDetailRepository;
import com.apricart.consumer.repository.jpa.CustomerAddressRepository;
import com.apricart.consumer.repository.jpa.CustomerRepository;
import com.apricart.consumer.repository.jpa.FeedBackRepository;
import com.apricart.consumer.repository.jpa.MissingProductRepository;
import com.apricart.consumer.repository.jpa.OtpRepository;
import com.apricart.consumer.repository.jpa.WishListRepository;
import com.apricart.consumer.security.dto.dto.AuthenticatedUserDto;
import com.apricart.consumer.security.dto.dto.OTPRequest;
import com.apricart.consumer.security.dto.request.ForgotPasswordRequest;
import com.apricart.consumer.security.dto.request.RegistrationRequest;
import com.apricart.consumer.security.dto.request.UpdatePasswordRequest;
import com.apricart.consumer.security.dto.response.LoginResponse;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.security.enums.UserRole;
import com.apricart.consumer.security.jwt.JwtTokenManager;
import com.apricart.consumer.security.mapper.CustomerMapper;
import com.apricart.consumer.service.*;
import com.apricart.consumer.utils.EmailUtils;
import com.apricart.consumer.utils.Utilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.*;
import static com.apricart.consumer.security.constants.Constants.EMAIL_SUBJECT_PASSWORD_RESET;
import static com.apricart.consumer.security.constants.Constants.EMAIL_SUBJECT_PASSWORD_UPDATE;
import static com.apricart.consumer.security.constants.ResponseMessage.*;

/**
 * Created on January, 2024
 *
 * @author Kashaf
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    protected static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserValidationService userValidationService;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    SalePersonService salePersonService;

    @Autowired
    JwtTokenManager jwtTokenManager;

    @Autowired
    EmailUtils emailUtils;

    @Autowired
    CityService cityService;

    @Autowired
    OTPService otpService;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    WishListRepository wishListRepository;

    @Autowired
    CustomerAddressRepository customerAddressRepository;

    @Autowired
    MissingProductRepository missingProductRepository;

    @Autowired
    CouponDetailRepository couponDetailRepository;

    @Autowired
    FeedBackRepository feedBackRepository;

    @Autowired
    OtpRepository otpRepository;

    @Override
    public Customer findByPhoneNumber(String phoneNumber) {
        return customerRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public Customer findByUsername(String username) {
        return customerRepository.findByUsername(username);
    }

    @Override
    public Customer findById(Long id, LanguageType lang) {
        return customerRepository.findById(id)
                .orElseThrow(() -> LanguageType.ARB.equals(lang)
                        ? new ResourceNotFoundException(String.format(CUSTOMER_ADDRESS_NOT_FOUND_ARABIC, id), true)
                        : new ResourceNotFoundException(String.format(CUSTOMER_ADDRESS_NOT_FOUND, id), true));
    }

    @Override
    public Customer findActiveCustomerById(Long id, LanguageType lang) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (optionalCustomer.isPresent()) {
            Customer activeCustomer = optionalCustomer.get();
            if (Boolean.TRUE.equals(activeCustomer.getIsActive())) {
                return activeCustomer;
            } else {
                throw new ResourceNotFoundException(
                        LanguageType.ARB.equals(lang) ? String.format(CUSTOMER_NOT_ACTIVE_WITH_ID_ARABIC, id)
                                : String.format(CUSTOMER_NOT_ACTIVE_WITH_ID, id),
                        true);
            }
        } else {
            if (LanguageType.ARB.equals(lang)) {
                throw new ResourceNotFoundException(String.format(CUSTOMER_ADDRESS_NOT_FOUND_ARABIC, id), true);
            } else {
                throw new ResourceNotFoundException(String.format(CUSTOMER_ADDRESS_NOT_FOUND, id), true);
            }
        }
    }

    @Override
    public Customer save(Customer user) {
        return customerRepository.save(user);
    }

    @Override
    public AuthenticatedUserDto findAuthenticatedUserByUsername(String username) {
        final Customer user = findByUsername(username);
        return CustomerMapper.INSTANCE.convertToAuthenticatedUserDto(user);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void updateAccessToken(Customer user, String accessToken) throws Exception {
        try {
            user.setAccessToken(accessToken);
            user.setUpdateDateTime(LocalDateTime.now());
            save(user);
        } catch (Exception e) {
            LOGGER.error("Failed to update access token for user with ID: {}", user.getId(), e);
            throw new Exception("Failed to update access token for user with ID : " + user.getId());
        }
    }

    @Override
    public ResponseEntity<GenericResponse<Object>> registration(RegistrationRequest request, LanguageType lang) {
        String phoneNumber = request.getPhoneNumber();
        String name = StringUtils.capitalize(request.getName());

        if (!Utilities.isValidSaudiPhoneNumber(phoneNumber)) {
            return lang.equals(LanguageType.ARB) ? Response.error(INVALID_PHONE_NUMBER_ERROR_ARABIC)
                    : Response.error(INVALID_PHONE_NUMBER_ERROR);
        }

        try {
            phoneNumber = Utilities.cleanSaudiPhoneNumber(phoneNumber);
            request.setUserName(phoneNumber);
            if (customerRepository.existsByPhoneNumber(phoneNumber)) {
                return lang.equals(LanguageType.ARB) ? Response.error(USER_EXISTS_ERROR_ARABIC)
                        : Response.error(USER_EXISTS_ERROR);
            }

            userValidationService.validateUser(request);
            Customer user = createUserFromRegistrationRequest(request, phoneNumber, lang);
            customerRepository.save(user);

            otpService.generateOTP(OTPRequest.builder().phoneNumber(phoneNumber).build(), lang);

            return lang.equals(LanguageType.ARB) ? Response.success(REGISTRATION_SUCCESSFUL_ARABIC)
                    : Response.success(REGISTRATION_SUCCESSFUL);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e.getCause());
            return lang.equals(LanguageType.ARB)
                    ? Response.error(ERROR_FAILED_ARABIC.concat(
                            e.getMessage() != null ? e.getMessage() : ((RegistrationException) e).getErrorMessage()))
                    : Response.error(ERROR_FAILED.concat(
                            e.getMessage() != null ? e.getMessage() : ((RegistrationException) e).getErrorMessage()));
        }
    }

    public Customer createUserFromRegistrationRequest(RegistrationRequest request, String phoneNumber,
            LanguageType languageType) throws UnknownHostException {
        Long cityId = request.getCityId();
        if (cityId == null) {
            List<City> cities = cityService.getAllCities();
            if (!cities.isEmpty()) {
                cityId = cities.get(0).getId();
            }
        }
        return Customer.builder()
                .name(request.getName())
                .arabicName(request.getArabicName())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .userRole(UserRole.USER)
                .isActive(false)
                .email(request.getEmail())
                .username(phoneNumber)
                .phoneNumber(phoneNumber)
                .createDateTime(LocalDateTime.now())
                .updateDateTime(LocalDateTime.now())
                .tradelicense(request.getTradelicense())
                .typeOfBusiness(request.getTypeOfBusiness())
                .city(cityService.findById(cityId, languageType))
                .salePerson(salePersonService.findSalePersonById(request.getSalesPersonId()))
                .ipAddress(String.valueOf(InetAddress.getLocalHost()))
                .build();
    }

    @Override
    public ResponseEntity<?> forgotPassword(ForgotPasswordRequest forgotPasswordRequest, LanguageType lang) {
        String phoneNumber = Utilities.cleanSaudiPhoneNumber(forgotPasswordRequest.getPhoneNumber());
        try {

            Customer customer = customerRepository.findByPhoneNumber(phoneNumber);
            if (customer == null) {
                return lang.equals(LanguageType.ARB) ? Response.notFound(ACCOUNT_NOT_FOUND_ARABIC)
                        : Response.notFound(ACCOUNT_NOT_FOUND);
            }

            String newPassword = bCryptPasswordEncoder.encode(forgotPasswordRequest.getPassword());
            customer.setPassword(newPassword);
            customer.setUpdateDateTime(LocalDateTime.now());
            customer.setIsActive(true);
            save(customer);

            String token = jwtTokenManager.generateToken(customer);
            updateAccessToken(customer, token);

            EmailUtils.SUBJECT = EMAIL_SUBJECT_PASSWORD_RESET;
            emailUtils.sendForgotPasswordEmail(customer);
            LoginResponse loginResponse = LoginResponse.builder()
                    .token(token)
                    .userId(customer.getId())
                    .name(customer.getName())
                    .phoneNumber(customer.getPhoneNumber())
                    .email(customer.getEmail())
                    .build();

            return lang.equals(LanguageType.ARB) ? Response.success(PASSWORD_UPDATED_SUCCESS_ARABIC, loginResponse)
                    : Response.success(PASSWORD_UPDATED_SUCCESS, loginResponse);
        } catch (Exception e) {
            log.error("Error : {}", e.getMessage(), e);
            return lang.equals(LanguageType.ARB) ? Response.error(PASSWORD_RESET_FAILED_ARABIC)
                    : Response.error(PASSWORD_RESET_FAILED);
        }
    }

    @Override
    public ResponseEntity<?> updatePassword(UpdatePasswordRequest updatePasswordRequest, Customer customer,
            LanguageType lang) {
        if (!Boolean.TRUE.equals(customer.getIsActive())) {
            return lang.equals(LanguageType.ARB) ? Response.error(USER_NOT_ACTIVE_ERROR_MESSAGE_ARABIC)
                    : Response.error(USER_NOT_ACTIVE_ERROR_MESSAGE);
        }

        if (!bCryptPasswordEncoder.matches(updatePasswordRequest.getCurrentPassword(), customer.getPassword())) {
            return lang.equals(LanguageType.ARB) ? Response.error(INCORRECT_PASSWORD_ERROR_MESSAGE_ARABIC)
                    : Response.error(INCORRECT_PASSWORD_ERROR_MESSAGE);
        }

        if (bCryptPasswordEncoder.matches(updatePasswordRequest.getNewPassword(), customer.getPassword())) {
            return lang.equals(LanguageType.ARB) ? Response.error(SAME_AS_OLD_PASSWORD_ERROR_MESSAGE_ARABIC)
                    : Response.error(SAME_AS_OLD_PASSWORD_ERROR_MESSAGE);
        }

        try {
            customer.setPassword(bCryptPasswordEncoder.encode(updatePasswordRequest.getNewPassword()));
            customer.setUpdateDateTime(LocalDateTime.now());
            customer.setIsActive(true);
            save(customer);

            Otp otp = otpService.findByPhoneNumber(customer.getPhoneNumber());
            otp.setOtp("");
            otpService.save(otp);

            final String token = jwtTokenManager.generateToken(customer);
            updateAccessToken(customer, token);

            EmailUtils.SUBJECT = EMAIL_SUBJECT_PASSWORD_UPDATE;
            emailUtils.sendPasswordResetEmail(customer);
            LoginResponse loginResponse = LoginResponse.builder()
                    .token(token)
                    .userId(customer.getId())
                    .name(customer.getName())
                    .phoneNumber(customer.getPhoneNumber())
                    .email(customer.getEmail())
                    .build();

            return lang.equals(LanguageType.ARB)
                    ? Response.success(PASSWORD_UPDATE_SUCCESS_MESSAGE_ARABIC, loginResponse)
                    : Response.success(PASSWORD_UPDATE_SUCCESS_MESSAGE, loginResponse);
        } catch (Exception e) {
            log.error("Exception occurred during password update", e);
            return lang.equals(LanguageType.ARB) ? Response.error(PASSWORD_UPDATE_FAILURE_MESSAGE_ARABIC)
                    : Response.error(PASSWORD_UPDATE_FAILURE_MESSAGE);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResponseEntity<?> deleteAccount(Customer customer, LanguageType lang) {
        try {
            if (customer == null || customer.getId() == null) {
                return lang.equals(LanguageType.ARB) ? Response.notFound(ACCOUNT_NOT_FOUND_ARABIC)
                        : Response.notFound(ACCOUNT_NOT_FOUND);
            }

            Long customerId = customer.getId();
            String phoneNumber = customer.getPhoneNumber();

            cartRepository.deleteByCustomer(customer);
            wishListRepository.deleteByCustomer(customer);
            missingProductRepository.deleteByCustomerId(customerId);

            List<CustomerAddress> addresses = customerAddressRepository.findByCustomerId(customerId);
            for (CustomerAddress address : addresses) {
                address.setActive(false);
                address.setAddressDetail(null);
                address.setAddressLatitude(null);
                address.setAddressLongitude(null);
                customerAddressRepository.save(address);
            }

            if (StringUtils.isNotBlank(phoneNumber)) {
                otpRepository.deleteByPhoneNumber(phoneNumber);
                List<FeedBack> feedbacks = feedBackRepository.findByPhoneNumber(phoneNumber);
                for (FeedBack feedback : feedbacks) {
                    feedback.setName("Deleted User");
                    feedback.setPhoneNumber("deleted_" + customerId);
                    feedback.setEmail("deleted_" + customerId + "@deleted.invalid");
                    feedBackRepository.save(feedback);
                }
            }

            List<CouponDetail> couponDetails = couponDetailRepository.findByCustomerId(customerId);
            for (CouponDetail couponDetail : couponDetails) {
                couponDetail.setPhoneNumber("deleted_" + customerId);
                couponDetailRepository.save(couponDetail);
            }

            String deletedMarker = "deleted_" + customerId;
            customer.setName("Deleted User");
            customer.setArabicName("مستخدم محذوف");
            customer.setUsername(deletedMarker);
            customer.setEmail(deletedMarker + "@deleted.invalid");
            customer.setPhoneNumber(deletedMarker);
            customer.setPassword(bCryptPasswordEncoder.encode(UUID.randomUUID().toString()));
            customer.setAccessToken(null);
            customer.setIsActive(false);
            customer.setIpAddress(null);
            customer.setTradelicense(null);
            customer.setTypeOfBusiness(null);
            customer.setCustomerId(null);
            customer.setSalePerson(null);
            customer.setCity(null);
            customer.setUpdateDateTime(LocalDateTime.now());
            customerRepository.save(customer);

            LOGGER.info("Deleted account for customer id {}", customerId);
            return lang.equals(LanguageType.ARB)
                    ? Response.success(ACCOUNT_DELETED_SUCCESSFULLY_ARABIC)
                    : Response.success(ACCOUNT_DELETED_SUCCESSFULLY);
        } catch (Exception e) {
            LOGGER.error("Failed to delete account for customer {}",
                    customer != null ? customer.getId() : null, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return lang.equals(LanguageType.ARB) ? Response.error(ACCOUNT_DELETE_FAILED_ARABIC)
                    : Response.error(ACCOUNT_DELETE_FAILED);
        }
    }
}
