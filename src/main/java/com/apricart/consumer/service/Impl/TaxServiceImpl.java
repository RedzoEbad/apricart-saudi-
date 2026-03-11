package com.apricart.consumer.service.Impl;

import com.apricart.consumer.enity.Tax;
import com.apricart.consumer.exceptions.ResourceNotFoundException;
import com.apricart.consumer.repository.jpa.TaxRepository;
import com.apricart.consumer.security.dto.request.TaxRequestDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.TaxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaxServiceImpl implements TaxService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaxServiceImpl.class);
    private static final String TAX_ENG = "Tax";
    private static final String TAX_ARB = "الضريبة";
    private static final String INVALID_VALUES_PROVIDED_ARABIC = "تم تقديم قيم غير صالحة";
    private static final String INVALID_VALUES_PROVIDED = "Invalid values are provided";

    @Autowired
    private TaxRepository taxRepository;

    @Override
    public List<Tax> getAllTaxes() {
        LOGGER.info("Retrieving all taxes");
        return taxRepository.findAll();
    }

    @Override
    public Tax findById(Long id, LanguageType languageType) {
        LOGGER.info("Finding tax by id: {}", id);
        return taxRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Tax with id {} not found", id);
                    return LanguageType.ARB.equals(languageType) ? new ResourceNotFoundException(TAX_ARB, id, true) : new ResourceNotFoundException(TAX_ENG, id, false);
                });
    }

    @Override
    public List<Tax> findByName(String name) {
        LOGGER.info("Finding tax by name: {}", name);
        return taxRepository.findByTaxName(name);
    }

    @Override
    public List<Tax> findByType(String type) {
        LOGGER.info("Finding taxes by type: {}", type);
        return taxRepository.findByTaxType(type);
    }

    @Override
    public List<Tax> findByCountryCode(String countryCode) {
        LOGGER.info("Finding tax by country code: {}", countryCode);
        return taxRepository.findByCountryCode(countryCode);
    }

    @Override
    public Tax findByAuthority(String authority) {
        LOGGER.info("Finding tax by authority: {}", authority);
        return taxRepository.findByTaxAuthorityId(authority);
    }

    @Override
    public void addTax(TaxRequestDTO taxRequestDTO) {
        LOGGER.info("Adding tax: {}", taxRequestDTO);
        save(Tax.fromDTO(taxRequestDTO));
    }

    public Tax save(Tax tax) {
        LOGGER.info("Saving tax: {}", tax);
        return taxRepository.save(tax);
    }

    @Override
    public Tax updateTax(TaxRequestDTO taxRequestDTO, LanguageType languageType) {
        LOGGER.info("Updating tax: {}", taxRequestDTO);
        Tax existingTax = findById(taxRequestDTO.getId(), languageType);
        existingTax.setTaxName(taxRequestDTO.getTaxName() == null ? existingTax.getTaxName() : taxRequestDTO.getTaxName());
        existingTax.setTaxType(taxRequestDTO.getTaxType() == null ? existingTax.getTaxType() : taxRequestDTO.getTaxType());
        existingTax.setTaxFactor(taxRequestDTO.getTaxFactor() == null ? existingTax.getTaxFactor() : taxRequestDTO.getTaxFactor());
        existingTax.setTaxAuthorityId(taxRequestDTO.getTaxAuthorityId() == null ? existingTax.getTaxAuthorityId() : taxRequestDTO.getTaxAuthorityId());
        existingTax.setTaxPercentage(taxRequestDTO.getTaxPercentage() == 0.0 ? existingTax.getTaxPercentage() : taxRequestDTO.getTaxPercentage());
        existingTax.setTdsPayableAccountId(taxRequestDTO.getTdsPayableAccountId() == null ? existingTax.getTdsPayableAccountId() : taxRequestDTO.getTdsPayableAccountId());
        existingTax.setTaxAuthorityName(taxRequestDTO.getTaxAuthorityName() == null ? existingTax.getTaxAuthorityName() : taxRequestDTO.getTaxAuthorityName());
        existingTax.setTaxSpecificType(taxRequestDTO.getTaxSpecificType() == null ? existingTax.getTaxSpecificType() : taxRequestDTO.getTaxSpecificType());
        existingTax.setCountryCode(taxRequestDTO.getCountryCode() == null ? existingTax.getCountryCode() : taxRequestDTO.getCountryCode());
        existingTax.setPurchaseTaxExpenseAccountId(taxRequestDTO.getPurchaseTaxExpenseAccountId() == 0 ? existingTax.getPurchaseTaxExpenseAccountId() : taxRequestDTO.getPurchaseTaxExpenseAccountId());
        return save(existingTax);
    }

    @Override
    public void deleteTax(Long id, LanguageType languageType) {
        LOGGER.info("Deleting tax with id: {}", id);
        if (!taxRepository.existsById(id)) {
            LOGGER.error("Tax with id {} not found for deletion", id);
            throw LanguageType.ARB.equals(languageType) ? new ResourceNotFoundException(TAX_ARB, id, true) : new ResourceNotFoundException(TAX_ENG, id, false);
        }
        taxRepository.deleteById(id);
    }

    @Override
    public Double getTaxAmount(String percentage, String amount, LanguageType languageType) {
        LOGGER.info("Calculating tax amount with percentage: {} and amount: {}", percentage, amount);
        if (percentage == null || amount == null) {
            LOGGER.error("Invalid values provided for tax calculation");
            throw LanguageType.ARB.equals(languageType) ? new ResourceNotFoundException(INVALID_VALUES_PROVIDED, true) : new ResourceNotFoundException(INVALID_VALUES_PROVIDED_ARABIC, true);
        }
        return (Double.parseDouble(percentage) / 100) * Double.parseDouble(amount);
    }
}
