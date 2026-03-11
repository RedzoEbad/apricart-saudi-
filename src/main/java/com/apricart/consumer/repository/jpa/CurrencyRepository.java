package com.apricart.consumer.repository.jpa;

import com.apricart.consumer.enity.City;
import com.apricart.consumer.enity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Currency findByCurrencyCode(String currencyCode);
    List<Currency> findByCurrencySymbol(String currencySymbol);

}
