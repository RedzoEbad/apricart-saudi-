package com.apricart.consumer.repository.jpa;

import com.apricart.consumer.enity.MissingProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created on Feb, 2024
 *
 * @author Kashaf Arshad
 */
public interface MissingProductRepository extends JpaRepository<MissingProduct, Long> {
    List<MissingProduct> findByCustomerId(Long customerId);
    void deleteByCustomerId(Long customerId);


}
