package com.apricart.consumer.service;

import com.apricart.consumer.enity.Tax;
import com.apricart.consumer.security.dto.request.TaxRequestDTO;
import com.apricart.consumer.security.enums.LanguageType;

import java.util.List;

public interface TaxService {
    List<Tax> getAllTaxes();
    Tax findById(Long id, LanguageType languageType);
    List<Tax> findByName(String name);
    List<Tax> findByType(String type);
    List<Tax> findByCountryCode(String countryCode);
    Tax findByAuthority(String authority);
    void addTax(TaxRequestDTO taxRequestDTO);
    Tax updateTax(TaxRequestDTO taxRequestDTO, LanguageType languageType);
    void deleteTax(Long id, LanguageType languageType);
    Double getTaxAmount(String percentage, String Amount, LanguageType languageType);
}
