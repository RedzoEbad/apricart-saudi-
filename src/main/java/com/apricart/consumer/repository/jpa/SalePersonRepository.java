package com.apricart.consumer.repository.jpa;

import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.enity.SalePerson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created on Feb, 2024
 *
 * @author Kashaf Arshad
 */
public interface SalePersonRepository extends JpaRepository<SalePerson, Long> {


}
