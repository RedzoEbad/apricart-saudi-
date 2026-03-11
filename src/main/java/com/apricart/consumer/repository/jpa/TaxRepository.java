package com.apricart.consumer.repository.jpa;

import com.apricart.consumer.enity.Currency;
import com.apricart.consumer.enity.Tax;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created on Feb, 2024
 *
 * @author Kashaf Arshad
 */
public interface TaxRepository extends JpaRepository<Tax, Long> {
    List<Tax> findByTaxName(String name);
    List<Tax> findByTaxType(String type);
    List<Tax> findByCountryCode(String countryCode);
    Tax findByTaxAuthorityId(String id);


}
