package com.apricart.consumer.service;

import com.apricart.consumer.enity.PriceList;
import com.apricart.consumer.security.dto.request.PriceListRequestDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.security.enums.PriceBookType;

import java.util.List;

public interface PriceListService {
    List<PriceList> getAllPriceLists();
    PriceList findById(Long id, LanguageType languageType);
    List<PriceList> findByCurrencyId(Long id, LanguageType languageType);
    PriceList findByName(String name);
    List<PriceList> findByPriceBook(PriceBookType priceBook);
    List<PriceList> getActivePriceLists(LanguageType lang);
    PriceList updatePriceListStatusById(Long id, Boolean status, LanguageType languageType);
    void addPriceList(PriceListRequestDTO priceListRequestDTO, LanguageType languageType);
    PriceList updatePriceList(PriceListRequestDTO priceListRequestDTO, LanguageType languageType);
    void deletePriceList(Long id, LanguageType languageType);
}
