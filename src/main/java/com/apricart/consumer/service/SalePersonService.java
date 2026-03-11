package com.apricart.consumer.service;

import com.apricart.consumer.enity.SalePerson;
import com.apricart.consumer.security.dto.request.SalePersonRequest;
import com.apricart.consumer.security.dto.request.SalesPersonRequestDTO;
import com.apricart.consumer.security.enums.LanguageType;

import java.util.List;

public interface SalePersonService {
    List<SalePerson> getAll();
    List<SalePerson> getActiveSalePersons();

    SalePerson findSalePersonById(Long id);

    void addSalePerson(SalesPersonRequestDTO salePersonRequest, LanguageType languageType);

    SalePerson updateSalePerson(SalesPersonRequestDTO salePersonRequest, LanguageType languageType);

    void markInactive(Long id);
}
