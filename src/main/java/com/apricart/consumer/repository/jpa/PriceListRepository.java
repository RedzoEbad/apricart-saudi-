package com.apricart.consumer.repository.jpa;

import com.apricart.consumer.enity.Currency;
import com.apricart.consumer.enity.PriceList;
import com.apricart.consumer.security.enums.PriceBookType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceListRepository extends JpaRepository<PriceList, Long> {

    PriceList findByName(String name);
    List<PriceList> findByPriceBookType(PriceBookType priceBookType);
    List<PriceList> findByCurrency(Currency currency);
}
