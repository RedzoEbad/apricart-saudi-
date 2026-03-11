package com.apricart.consumer.service;

import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.security.dto.request.UpdateProfileRequest;
import com.apricart.consumer.security.enums.LanguageType;
import org.springframework.http.ResponseEntity;

public interface ProfileService {
    Customer findCustomerProfile(Customer customer, LanguageType lang);
    ResponseEntity<?> updateProfile(UpdateProfileRequest updateProfileRequest, Customer customer, LanguageType lang);

}
