package com.apricart.consumer.controller;

import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.enity.CustomerAddress;
import com.apricart.consumer.generic.GenericResponse;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.security.constants.ResponseMessage;
import com.apricart.consumer.security.dto.request.CustomerAddressRequestDTO;
import com.apricart.consumer.security.dto.response.CustomerAddressResponseDTO;
import com.apricart.consumer.security.enums.AddressType;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.BaseService;
import com.apricart.consumer.service.CustomerAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.*;
import static com.apricart.consumer.security.constants.ResponseMessage.*;

@RestController
@RequestMapping("/v1/address")
@Api(value = "Address Controller", tags = {"Address"})
public class CustomerAddressController {

    @Autowired
    BaseService baseService;
    @Autowired
    private CustomerAddressService customerAddressService;

    @ApiOperation(value = "Get by Id", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping("/{addressId}")
    public ResponseEntity<GenericResponse<CustomerAddressResponseDTO>> findById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                @RequestHeader("Language") LanguageType lang,
                                                                                @PathVariable Long addressId) {
        CustomerAddressResponseDTO customerAddress = CustomerAddress.toDTO(customerAddressService.findById(addressId, lang));
        return customerAddress != null ? Response.success(customerAddress) : Response.notFound();
    }

    @ApiOperation(value = "Get customer by Id ", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping("/customer")
    public ResponseEntity<GenericResponse<List<CustomerAddressResponseDTO>>> findCustomerById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                              @RequestHeader("Language") LanguageType lang,
                                                                                              HttpServletRequest request) {
        Customer customer = baseService.resolveUser(request);
        List<CustomerAddressResponseDTO> customerAddress = CustomerAddress.toDTOList(customerAddressService.findByCustomerId(customer.getId()));
        return !customerAddress.isEmpty() ? Response.success(customerAddress) : Response.notFound();
    }

    @ApiOperation(value = "Get by address type ", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping("/customer/type/{addressType}")
    public ResponseEntity<GenericResponse<List<CustomerAddressResponseDTO>>> findByAddressType(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                               @RequestHeader("Language") LanguageType lang,
                                                                                               @PathVariable AddressType addressType,
                                                                                               HttpServletRequest request) {
        Customer customer = baseService.resolveUser(request);
        List<CustomerAddressResponseDTO> customerAddress = CustomerAddress.toDTOList(customerAddressService.findByAddressType(customer, addressType));
        return !customerAddress.isEmpty() ? Response.success(customerAddress) : Response.notFound();
    }

    @ApiOperation(value = "Disable Address by address type and Id ", authorizations = {@Authorization(value = "jwtToken")})
    @DeleteMapping("/disable/{addressType}/{addressId}")
    public ResponseEntity<GenericResponse<String>> disableAddressByTypeAndId(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                             @PathVariable AddressType addressType,
                                                                             @PathVariable Long addressId,
                                                                             @RequestHeader("Language") LanguageType lang,
                                                                             HttpServletRequest request) {
        Customer customer = baseService.resolveUser(request);
        customerAddressService.disableAddressByType(customer, addressType, addressId);
        return lang.equals(LanguageType.ARB) ? Response.success(CUSTOMER_ADDRESS_DISABLED_SUCCESSFULLY_ARABIC) : Response.success(CUSTOMER_ADDRESS_DISABLED_SUCCESSFULLY);
    }

    @ApiOperation(value = "Enable Address by address type and Id ", authorizations = {@Authorization(value = "jwtToken")})
    @PutMapping("/enable/{addressType}/{addressId}")
    public ResponseEntity<GenericResponse<String>> enableAddressByType(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                       @PathVariable AddressType addressType,
                                                                       @PathVariable Long addressId,
                                                                       @RequestHeader("Language") LanguageType lang,
                                                                       HttpServletRequest request) {
        Customer customer = baseService.resolveUser(request);
        customerAddressService.enableAddressByType(customer, addressType, addressId);
        return lang.equals(LanguageType.ARB) ? Response.success(CUSTOMER_ADDRESS_ENABLED_SUCCESSFULLY_ARABIC) : Response.success(CUSTOMER_ADDRESS_ENABLED_SUCCESSFULLY);
    }

    @ApiOperation(value = "Disable Address by Customer", authorizations = {@Authorization(value = "jwtToken")})
    @DeleteMapping("/disableCustomer")
    public ResponseEntity<GenericResponse<String>> disableAddressByCustomerId(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                              @RequestHeader("Language") LanguageType lang,
                                                                              HttpServletRequest request) {
        Customer customer = baseService.resolveUser(request);
        customerAddressService.disableAddressByCustomerId(customer.getId(), lang);
        return lang.equals(LanguageType.ARB) ? Response.success(CUSTOMER_ADDRESS_DISABLED_SUCCESSFULLY_ARABIC) : Response.success(CUSTOMER_ADDRESS_DISABLED_SUCCESSFULLY);
    }

    @ApiOperation(value = "Enable Address by Customer", authorizations = {@Authorization(value = "jwtToken")})
    @PutMapping("/enableCustomer")
    public ResponseEntity<GenericResponse<String>> enableAddressByCustomerId(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                             @RequestHeader("Language") LanguageType lang,
                                                                             HttpServletRequest request) {
        Customer customer = baseService.resolveUser(request);
        if (Boolean.TRUE.equals(customerAddressService.findActiveCustomerById(customer.getId(), lang).getIsActive())) {
            customerAddressService.enableAddressByCustomerId(customer.getId(), lang);
            return lang.equals(LanguageType.ARB) ? Response.success(CUSTOMER_ADDRESS_ENABLED_SUCCESSFULLY_ARABIC) : Response.success(CUSTOMER_ADDRESS_ENABLED_SUCCESSFULLY);
        } else {
            return lang.equals(LanguageType.ARB) ? Response.success(CUSTOMER_NOT_ACTIVE_ARABIC) : Response.success(CUSTOMER_NOT_ACTIVE);
        }

    }

    @ApiOperation(value = "Disable Address by Address Id", authorizations = {@Authorization(value = "jwtToken")})
    @DeleteMapping("/disable/{addressId}")
    public ResponseEntity<GenericResponse<String>> disableAddressById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                      @PathVariable Long addressId,
                                                                      @RequestHeader("Language") LanguageType lang) {
        customerAddressService.disableAddressById(addressId, lang);
        return lang.equals(LanguageType.ARB) ? Response.success(CUSTOMER_ADDRESS_DISABLED_SUCCESSFULLY_ARABIC) : Response.success(CUSTOMER_ADDRESS_DISABLED_SUCCESSFULLY);
    }

    @ApiOperation(value = "Delete Address by Address Id", authorizations = {@Authorization(value = "jwtToken")})
    @DeleteMapping("/{addressId}")
    public ResponseEntity<GenericResponse<String>> removeAddressById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                     @PathVariable Long addressId,
                                                                     @RequestHeader("Language") LanguageType lang) {
        customerAddressService.disableAddressById(addressId, lang);
        return lang.equals(LanguageType.ARB) ? Response.success(CUSTOMER_ADDRESS_REMOVED_SUCCESSFULLY_ARABIC) : Response.success(CUSTOMER_ADDRESS_REMOVED_SUCCESSFULLY);
    }

    @ApiOperation(value = "Enable Address by Address Id", authorizations = {@Authorization(value = "jwtToken")})
    @PutMapping("/enable/{addressId}")
    public ResponseEntity<GenericResponse<String>> enableAddressById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                     @PathVariable Long addressId,
                                                                     @RequestHeader("Language") LanguageType lang) {
        customerAddressService.enableAddressById(addressId, lang);
        return lang.equals(LanguageType.ARB) ? Response.success(CUSTOMER_ADDRESS_ENABLED_SUCCESSFULLY_ARABIC) : Response.success(CUSTOMER_ADDRESS_ENABLED_SUCCESSFULLY);
    }

    @ApiOperation(value = "Save Address by Address Id", authorizations = {@Authorization(value = "jwtToken")})
    @PostMapping("/saveOrUpdate")
    public ResponseEntity<GenericResponse<String>> addAddressByCustomer(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                        @Valid @RequestBody CustomerAddressRequestDTO dto,
                                                                        @RequestHeader("Language") LanguageType lang,
                                                                        HttpServletRequest request) {
        Customer customer = baseService.resolveUser(request);
        customerAddressService.addAddressByCustomer(dto, customer, lang);
        return (dto.getId() == null) ? Response.created() : lang.equals(LanguageType.ARB) ? Response.success(CUSTOMER_ADDRESS_UPDATED_SUCCESSFULLY_ARABIC) : Response.success(ResponseMessage.CUSTOMER_ADDRESS_UPDATED_SUCCESSFULLY);
    }

}

