package com.apricart.consumer.repository.jpa;

import com.apricart.consumer.enity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created on Feb, 2024
 *
 * @author Kashaf Arshad
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	List<Customer> findAllByOrderByUpdateDateTimeDesc();
	Customer findByUsername(String username);
	Customer findActiveCustomerById(Long id);

	Customer findByPhoneNumber(String phoneNumber);

	boolean existsByPhoneNumber(String phoneNumber);

	boolean existsByEmail(String email);

	boolean existsByUsername(String username);

}
