package com.apricart.consumer.service.Impl;

import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.enity.CustomerAddress;
import com.apricart.consumer.exceptions.ResourceNotFoundException;
import com.apricart.consumer.repository.jpa.CustomerAddressRepository;
import com.apricart.consumer.security.dto.request.CustomerAddressRequestDTO;
import com.apricart.consumer.security.enums.AddressType;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.CityService;
import com.apricart.consumer.service.CustomerAddressService;
import com.apricart.consumer.service.CustomerService;
import com.apricart.consumer.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.CUSTOMER_ADDRESS_NOT_FOUND_ARABIC;
import static com.apricart.consumer.security.constants.ArabicResponseMessages.CUSTOMER_ADDRESS_REFERENCE_ERROR_ARABIC;
import static com.apricart.consumer.security.constants.ResponseMessage.CUSTOMER_ADDRESS_NOT_FOUND;
import static com.apricart.consumer.security.constants.ResponseMessage.CUSTOMER_ADDRESS_REFERENCE_ERROR;

@Service
public class CustomerAddressServiceImpl implements CustomerAddressService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerAddressServiceImpl.class);

    @Autowired
    private CityService cityService;

    @Autowired
    private CustomerAddressRepository customerAddressRepository;

    @Autowired
    private CustomerService customerService;
    @Autowired
    private OrderService orderService;

    @Override
    public CustomerAddress findById(Long id, LanguageType languageType) {
        LOGGER.info("Finding customer address by id: {}", id);
        return customerAddressRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Customer address with id {} not found", id);
                    return new ResourceNotFoundException(getAddressNotFoundText(languageType, id), true);
                });
    }

    @Override
    public List<CustomerAddress> findByCustomerId(Long customerId) {
        LOGGER.info("Finding customer addresses by customer id: {}", customerId);
        return customerAddressRepository.findByCustomerId(customerId)
                .stream()
                .filter(CustomerAddress::isActive)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerAddress> findByAddressType(Customer customer, AddressType addressType) {
        LOGGER.info("Finding customer addresses by customer id {} and address type: {}", customer.getId(), addressType);
        return customerAddressRepository.findByCustomerIdAndAddressType(customer.getId(), addressType);
    }

    @Override
    public Customer findActiveCustomerById(Long id, LanguageType lang) {
        LOGGER.info("Finding active customer by id: {}", id);
        return customerService.findActiveCustomerById(id, lang);
    }

    @Override
    public void addAddressByCustomer(CustomerAddressRequestDTO customerAddressRequestDTO, Customer customer, LanguageType lang) {
        LOGGER.info("Adding address by customer: {}", customerAddressRequestDTO);
        CustomerAddress customerAddress;
        if (customerAddressRequestDTO.getId() != null) {
            customerAddress = findById(customerAddressRequestDTO.getId(), lang);
            customerAddressRequestDTO.setCreateDateTime(customerAddress.getCreateDateTime());
        }
        customerAddress = CustomerAddress.fromDTO(customerAddressRequestDTO);
        customerAddress.setCity(cityService.findById(customerAddressRequestDTO.getCityId(), lang));
        customerAddress.setCustomer(customer);
        save(customerAddress);
    }

    public CustomerAddress save(CustomerAddress customerAddress) {
        LOGGER.info("Saving customer address: {}", customerAddress);
        return customerAddressRepository.save(customerAddress);
    }

    @Override
    public void disableAddressByType(Customer customer, AddressType addressType, Long addressId) {
        LOGGER.info("Disabling address by type: {}, customer: {}, addressId: {}", addressType, customer, addressId);
        List<CustomerAddress> existingCustomerAddress = findByAddressType(customer, addressType);
        boolean found = false;
        for (CustomerAddress customerAddress : existingCustomerAddress) {
            if (customerAddress.getId().equals(addressId)) {
                customerAddress.setActive(false);
                save(customerAddress);
                found = true;
                break;
            }
        }

        if (!found) {
            LOGGER.error("Address with type {} and id {} not found for customer {}", addressType, addressId, customer);
            throw new ResourceNotFoundException("Address", addressType, addressId);

        }
    }

    @Override
    public void enableAddressByType(Customer customer, AddressType addressType, Long addressId) {
        LOGGER.info("Enabling address by type: {}, customer: {}, addressId: {}", addressType, customer, addressId);
        List<CustomerAddress> existingCustomerAddress = findByAddressType(customer, addressType);
        boolean found = false;
        for (CustomerAddress customerAddress : existingCustomerAddress) {
            if (customerAddress.getId().equals(addressId)) {
                customerAddress.setActive(true);
                save(customerAddress);
                found = true;
                break;
            }
        }

        if (!found) {
            LOGGER.error("Address with type {} and id {} not found for customer {}", addressType, addressId, customer);
            throw new ResourceNotFoundException("Address", addressType, addressId);
        }
    }

    @Override
    public void disableAddressByCustomerId(Long customerId, LanguageType lang) {
        LOGGER.info("Disabling all addresses for customer with id: {}", customerId);
        boolean found = false;
        List<CustomerAddress> existingCustomerAddress = findByCustomerId(customerId);
        for (CustomerAddress customerAddress : existingCustomerAddress) {
            customerAddress.setActive(false);
            save(customerAddress);
            found = true;
        }
        if (!found) {
            LOGGER.error("No customer addresses found for customer with id {}", customerId);
            throw new ResourceNotFoundException(getAddressNotFoundText(lang, customerId), true);
        }
    }

    @Override
    public void enableAddressByCustomerId(Long customerId, LanguageType lang) {
        LOGGER.info("Enabling all addresses for customer with id: {}", customerId);
        boolean found = false;
        List<CustomerAddress> existingCustomerAddress = findByCustomerId(customerId);
        for (CustomerAddress customerAddress : existingCustomerAddress) {
            customerAddress.setActive(true);
            save(customerAddress);
            found = true;
        }
        if (!found) {
            LOGGER.error("No customer addresses found for customer with id {}", customerId);
            throw new ResourceNotFoundException(getAddressNotFoundText(lang, customerId), true);
        }
    }

    @Override
    public void disableAddressById(Long id, LanguageType lang) {
        LOGGER.info("Disabling address by id: {}", id);
        CustomerAddress existingCustomerAddress = findById(id, lang);
        if (existingCustomerAddress != null) {
            existingCustomerAddress.setActive(false);
            save(existingCustomerAddress);
        } else {
            LOGGER.error("Customer address with id {} not found", id);
            throw new ResourceNotFoundException(getAddressNotFoundText(lang, id), true);

        }
    }

    @Override
    public void enableAddressById(Long id, LanguageType lang) {
        LOGGER.info("Enabling address by id: {}", id);
        CustomerAddress existingCustomerAddress = findById(id, lang);
        if (existingCustomerAddress != null) {
            existingCustomerAddress.setActive(true);
            save(existingCustomerAddress);
        } else {
            LOGGER.error("Customer address with id {} not found", id);
            throw new ResourceNotFoundException(getAddressNotFoundText(lang, id), true);

        }
    }
    @Override
    public void deleteAddressById(Long id, LanguageType lang) {
        LOGGER.info("Removing address by id: {}", id);
        if (!customerAddressRepository.existsById(id)) {

            LOGGER.error("Address with id {} not found for removing", id);
            throw new ResourceNotFoundException(getAddressNotFoundText(lang, id), true);
        }
        if (orderService.existsByCustomerAddress(findById(id, lang))) {
            LOGGER.error("Cannot remove address with id {} as it is referenced by an order", id);
            throw new ResourceNotFoundException(LanguageType.ARB.equals(lang) ? CUSTOMER_ADDRESS_REFERENCE_ERROR_ARABIC : CUSTOMER_ADDRESS_REFERENCE_ERROR, true);
        }
        if (!findById(id, lang).isActive()) {
            LOGGER.error("Address with id {} not found for removing", id);
            throw new ResourceNotFoundException(getAddressNotFoundText(lang, id), true);
        }
        customerAddressRepository.deleteById(id);
    }

    String getAddressNotFoundText(LanguageType languageType, Long id) {
        if (LanguageType.ARB.equals(languageType)) {
            return String.format(CUSTOMER_ADDRESS_NOT_FOUND_ARABIC, id);
        } else {
            return String.format(CUSTOMER_ADDRESS_NOT_FOUND, id);
        }
    }
}