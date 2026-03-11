package com.apricart.consumer.service.Impl;

import com.apricart.consumer.enity.Currency;
import com.apricart.consumer.enity.PriceList;
import com.apricart.consumer.exceptions.ResourceNotFoundException;
import com.apricart.consumer.repository.jpa.PriceListRepository;
import com.apricart.consumer.security.dto.request.PriceListRequestDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.security.enums.PriceBookType;
import com.apricart.consumer.service.CurrencyService;
import com.apricart.consumer.service.PriceListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.PRICE_LIST_STATUS_NOT_ACTIVE_ARABIC;
import static com.apricart.consumer.security.constants.ResponseMessage.PRICE_LIST_STATUS_NOT_ACTIVE;

@Service
public class PriceListServiceImpl implements PriceListService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PriceListServiceImpl.class);

    @Autowired
    private PriceListRepository priceListRepository;

    @Autowired
    private CurrencyService currencyService;
    private static final String PRICE_LIST_ENG = "Price List";
    private static final String PRICE_LIST_ARB = "قائمة الأسعار";

    @Override
    public List<PriceList> getAllPriceLists() {
        LOGGER.info("Retrieving all price lists");
        return priceListRepository.findAll();
    }

    @Override
    public PriceList findById(Long id, LanguageType languageType) {
        LOGGER.info("Finding price list by id: {}", id);
        return priceListRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Price list with id {} not found", id);
                    return LanguageType.ARB.equals(languageType) ? new ResourceNotFoundException(PRICE_LIST_ARB, id, true) : new ResourceNotFoundException(PRICE_LIST_ENG, id, false);
                });
    }

    @Override
    public List<PriceList> findByCurrencyId(Long id, LanguageType languageType) {
        LOGGER.info("Finding price lists by currency id: {}", id);
        Currency currency = currencyService.findById(id, languageType);
        return priceListRepository.findByCurrency(currency);
    }

    @Override
    public PriceList findByName(String name) {
        LOGGER.info("Finding price list by name: {}", name);
        return priceListRepository.findByName(name);
    }

    @Override
    public List<PriceList> findByPriceBook(PriceBookType priceBook) {
        LOGGER.info("Finding price lists by price book type: {}", priceBook);
        return priceListRepository.findByPriceBookType(priceBook);
    }

    @Override
    public List<PriceList> getActivePriceLists(LanguageType lang) {
        LOGGER.info("Retrieving active price lists");
        List<PriceList> priceLists = priceListRepository.findAll().stream()
                .filter(PriceList::isActive)
                .collect(Collectors.toList());

        if (!priceLists.isEmpty()) {
            return priceLists;
        } else {
            LOGGER.error("No active price lists found");
            throw new ResourceNotFoundException(LanguageType.ARB.equals(lang) ? PRICE_LIST_STATUS_NOT_ACTIVE_ARABIC : PRICE_LIST_STATUS_NOT_ACTIVE, true);
        }
    }

    @Override
    public PriceList updatePriceListStatusById(Long id, Boolean status, LanguageType languageType) {
        LOGGER.info("Updating status of price list with id: {} to status: {}", id, status);
        PriceList existingPriceList = findById(id, languageType);
        existingPriceList.setActive(status == null ? existingPriceList.isActive() : status);
        return save(existingPriceList);
    }

    @Override
    public void addPriceList(PriceListRequestDTO priceListRequestDTO, LanguageType lang) {
        LOGGER.info("Adding price list: {}", priceListRequestDTO);
        PriceList priceList;
        if (priceListRequestDTO.getId() != null) {
            priceList = findById(priceListRequestDTO.getId(), lang);
        }
        priceList = PriceList.fromDTO(priceListRequestDTO);
        priceList.setCurrency(currencyService.findById(priceListRequestDTO.getCurrencyId(), lang));
        save(priceList);
    }

    @Override
    public PriceList updatePriceList(PriceListRequestDTO priceListRequestDTO, LanguageType languageType) {
        LOGGER.info("Updating price list: {}", priceListRequestDTO);
        PriceList existingPriceList = findById(priceListRequestDTO.getId(), languageType);

        existingPriceList.setName(priceListRequestDTO.getName() == null ? existingPriceList.getName() : priceListRequestDTO.getName());
        existingPriceList.setPriceBookType(priceListRequestDTO.getPriceBookType() == null ? existingPriceList.getPriceBookType() : priceListRequestDTO.getPriceBookType());
        existingPriceList.setIsIncrease(priceListRequestDTO.getIsIncrease() == null ? existingPriceList.getIsIncrease() : priceListRequestDTO.getIsIncrease());
        existingPriceList.setRoundingType(priceListRequestDTO.getRoundingType() == null ? existingPriceList.getRoundingType() : priceListRequestDTO.getRoundingType());
        existingPriceList.setSalesOrPurchaseType(priceListRequestDTO.getSalesOrPurchaseType() == null ? existingPriceList.getSalesOrPurchaseType() : priceListRequestDTO.getSalesOrPurchaseType());
        existingPriceList.setDescription(priceListRequestDTO.getDescription() == null ? existingPriceList.getDescription() : priceListRequestDTO.getDescription());
        existingPriceList.setCurrency(priceListRequestDTO.getCurrencyId() == null ? existingPriceList.getCurrency() : currencyService.findById(priceListRequestDTO.getCurrencyId(), languageType));
        return save(existingPriceList);
    }

    @Override
    public void deletePriceList(Long id, LanguageType languageType) {
        LOGGER.info("Deactivating price list for id: {}", id);
        PriceList existingPriceList = findById(id, languageType);
        if(existingPriceList.isActive()) {
            existingPriceList.setActive(false);
            save(existingPriceList);
        }
    }

    public PriceList save(PriceList priceList) {
        LOGGER.info("Saving price list: {}", priceList);
        return priceListRepository.save(priceList);
    }
}
