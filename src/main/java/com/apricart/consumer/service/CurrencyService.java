package com.apricart.consumer.service;

import com.apricart.consumer.enity.Currency;
import com.apricart.consumer.security.dto.request.CurrencyRequestDTO;
import com.apricart.consumer.security.enums.LanguageType;

import java.util.List;

public interface CurrencyService {
    List<Currency> getAllCurrencies();
    Currency findById(Long id, LanguageType languageType);
    Currency findByCurrencyCode(String currencyCode);
    List<Currency> findByCurrencySymbol(String currencySymbol);
    String formatCurrency(String value, Long id, LanguageType languageType);
    String getCurrencyFormat(Long id, LanguageType languageType);
    String getCurrencyPrecision(Long id, LanguageType languageType);
    List<Currency> getActiveCurrencies(LanguageType lang);
    void addCurrency(CurrencyRequestDTO currencyRequestDTO);
    Currency updateCurrency(CurrencyRequestDTO currencyRequestDTO, LanguageType languageType);
    void deleteCurrency(Long id, LanguageType languageType);
}
