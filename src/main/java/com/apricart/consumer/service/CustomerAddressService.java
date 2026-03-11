package com.apricart.consumer.service;

import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.enity.CustomerAddress;
import com.apricart.consumer.security.dto.request.CustomerAddressRequestDTO;
import com.apricart.consumer.security.enums.AddressType;
import com.apricart.consumer.security.enums.LanguageType;

import java.util.List;

public interface CustomerAddressService {
    CustomerAddress findById(Long id, LanguageType languageType);
    List<CustomerAddress> findByCustomerId(Long id);
    List<CustomerAddress> findByAddressType(Customer customer, AddressType addressType);
    Customer findActiveCustomerById(Long id, LanguageType lang);
    void addAddressByCustomer(CustomerAddressRequestDTO customerAddressRequestDTO, Customer customer, LanguageType languageType);
    void disableAddressByType(Customer customer, AddressType addressType, Long addressId);
    void enableAddressByType(Customer customer, AddressType addressType, Long addressId);
    void disableAddressByCustomerId(Long id, LanguageType languageType);
    void enableAddressByCustomerId(Long id, LanguageType languageType);
    void disableAddressById(Long id, LanguageType languageType);
    void enableAddressById(Long id, LanguageType languageType);
    void deleteAddressById(Long id, LanguageType lang);
}
