package com.apricart.consumer.repository.jpa;

import com.apricart.consumer.enity.CustomerAddress;
import com.apricart.consumer.security.enums.AddressType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Long> {

    List<CustomerAddress> findByCustomerIdAndAddressType(Long customerId, AddressType addressType);
    List<CustomerAddress> findByCustomerId(Long customerId);
}
