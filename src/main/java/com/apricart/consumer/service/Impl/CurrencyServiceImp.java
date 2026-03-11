package com.apricart.consumer.service.Impl;

import com.apricart.consumer.enity.Currency;
import com.apricart.consumer.exceptions.ResourceNotFoundException;
import com.apricart.consumer.repository.jpa.CurrencyRepository;
import com.apricart.consumer.security.dto.request.CurrencyRequestDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.CurrencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.CURRENCY_STATUS_NOT_ACTIVE_ARABIC;
import static com.apricart.consumer.security.constants.ResponseMessage.CURRENCY_STATUS_NOT_ACTIVE;
import static com.apricart.consumer.utils.Utilities.getFormatString;

@Service
public class CurrencyServiceImp implements CurrencyService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyServiceImp.class);
    private static final String CURRENCY_ENG = "Currency";
    private static final String CURRENCY_ARB = "العملة";

    @Autowired
    private CurrencyRepository currencyRepository;
    @Override
    public List<Currency> getAllCurrencies() {
        LOGGER.info("Retrieving all currencies");
        return currencyRepository.findAll();
    }

    @Override
    public Currency findById(Long id, LanguageType languageType) {
        LOGGER.info("Finding currency by id: {}", id);
        return currencyRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Currency with id {} not found", id);
                    return LanguageType.ARB.equals(languageType) ? new ResourceNotFoundException(CURRENCY_ARB, id, true) : new ResourceNotFoundException(CURRENCY_ENG, id, false);
                });
    }

    @Override
    public Currency findByCurrencyCode(String currencyCode) {
        LOGGER.info("Finding currency by currency code: {}", currencyCode);
        return currencyRepository.findByCurrencyCode(currencyCode);
    }

    @Override
    public List<Currency> findByCurrencySymbol(String currencySymbol) {
        LOGGER.info("Finding currencies by currency symbol: {}", currencySymbol);
        return currencyRepository.findByCurrencySymbol(currencySymbol);
    }

    @Override
    public List<Currency> getActiveCurrencies(LanguageType lang) {
        LOGGER.info("Retrieving active currencies");
        List<Currency> currencyList = currencyRepository.findAll().stream()
                .filter(Currency::isActive)
                .sorted(Comparator.comparingLong(Currency::getId))
                .collect(Collectors.toList());

        if (!currencyList.isEmpty()) {
            return currencyList;
        } else {
            LOGGER.error("No active currencies found");
            throw new ResourceNotFoundException(LanguageType.ARB.equals(lang) ? CURRENCY_STATUS_NOT_ACTIVE_ARABIC : CURRENCY_STATUS_NOT_ACTIVE, true);
        }
    }
    public Currency save(Currency currency) {
        LOGGER.info("Saving currency: {}", currency);
        return currencyRepository.save(currency);
    }

    @Override
    public void addCurrency(CurrencyRequestDTO currencyRequestDTO) {
        LOGGER.info("Adding currency: {}", currencyRequestDTO);
        save(Currency.fromDTO(currencyRequestDTO));
    }

    @Override
    public Currency updateCurrency(CurrencyRequestDTO currencyRequestDTO, LanguageType languageType) {
        LOGGER.info("Updating currency: {}", currencyRequestDTO);
        Currency existingCurrency = findById(currencyRequestDTO.getId(), languageType);
        existingCurrency.setCurrencyCode(currencyRequestDTO.getCurrencyCode() == null ? existingCurrency.getCurrencyCode() : currencyRequestDTO.getCurrencyCode());
        existingCurrency.setCurrencySymbol(currencyRequestDTO.getCurrencySymbol() == null ? existingCurrency.getCurrencySymbol() : currencyRequestDTO.getCurrencySymbol());
        existingCurrency.setCurrencyFormat(currencyRequestDTO.getCurrencyFormat() == null ? existingCurrency.getCurrencyFormat() : currencyRequestDTO.getCurrencyFormat());
        existingCurrency.setCurrencyPrecision(currencyRequestDTO.getCurrencyPrecision() == null ? existingCurrency.getCurrencyPrecision() : currencyRequestDTO.getCurrencyPrecision());

        return save(existingCurrency);
    }

    @Override
    public void deleteCurrency(Long id, LanguageType languageType) {
        LOGGER.info("Deactivating currency for id: {}", id);
        Currency existingCurrency = findById(id, languageType);
        if(existingCurrency.isActive()) {
            existingCurrency.setActive(false);
            save(existingCurrency);
        }
    }
    @Override
    public String formatCurrency(String value, Long id, LanguageType languageType) {
        Currency currency = currencyRepository.findById(id)
                .orElseThrow(() -> LanguageType.ARB.equals(languageType) ? new ResourceNotFoundException(CURRENCY_ARB, id, true) : new ResourceNotFoundException(CURRENCY_ENG, id, false));
        double numericValue = Double.parseDouble(value);

        int precision = Integer.parseInt(currency.getCurrencyPrecision());

        return getFormatString(precision, numericValue);
    }

    @Override
    public String getCurrencyFormat(Long id, LanguageType languageType) {
        LOGGER.info("Retrieving currency format for currency id: {}", id);
        Currency currency = currencyRepository.findById(id)
                .orElseThrow(() -> LanguageType.ARB.equals(languageType) ? new ResourceNotFoundException(CURRENCY_ARB, id, true) : new ResourceNotFoundException(CURRENCY_ENG, id, false));
        return currency.getCurrencyFormat();
    }

    @Override
    public String getCurrencyPrecision(Long id, LanguageType languageType) {
        LOGGER.info("Retrieving currency precision for currency id: {}", id);
        Currency currency = currencyRepository.findById(id)
                .orElseThrow(() -> LanguageType.ARB.equals(languageType) ? new ResourceNotFoundException(CURRENCY_ARB, id, true) : new ResourceNotFoundException(CURRENCY_ENG, id, false));
        return currency.getCurrencyPrecision();
    }
}
