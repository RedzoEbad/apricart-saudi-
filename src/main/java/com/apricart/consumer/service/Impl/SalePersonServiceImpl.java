package com.apricart.consumer.service.Impl;

import com.apricart.consumer.enity.SalePerson;
import com.apricart.consumer.exceptions.ResourceNotFoundException;
import com.apricart.consumer.repository.jpa.SalePersonRepository;
import com.apricart.consumer.security.dto.request.SalesPersonRequestDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.CityService;
import com.apricart.consumer.service.SalePersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SalePersonServiceImpl implements SalePersonService {
    protected static final Logger LOGGER = LoggerFactory.getLogger(SalePersonServiceImpl.class);

    @Autowired
    CityService cityService;
    @Autowired
    SalePersonRepository salePersonRepository;

    @Override
    public List<SalePerson> getAll() {
        LOGGER.info("Getting all sales person");
        return salePersonRepository.findAll();
    }
    @Override
    public List<SalePerson> getActiveSalePersons() {
        LOGGER.info("Getting active sale persons");
        return salePersonRepository.findAll().stream()
                .filter(SalePerson::getIsActive)
                .collect(Collectors.toList());
    }

    public SalePerson save(SalePerson salePerson) {
        LOGGER.info("Saving sale person");
        return salePersonRepository.save(salePerson);
    }

    @Override
    public SalePerson findSalePersonById(Long id) {
        LOGGER.info("Finding sale person by id: {}",id);
        return salePersonRepository.findById(id).orElse(null);
    }

    @Override
    public void addSalePerson(SalesPersonRequestDTO salePersonRequest, LanguageType languageType) {
        LOGGER.info("Adding sale person: {}",salePersonRequest);
        SalePerson salePerson = SalePerson.fromDTO(salePersonRequest);
        salePerson.setCity(cityService.findById(salePersonRequest.getCityId(), languageType));

         save(salePerson);
    }

    @Override
    public SalePerson updateSalePerson(SalesPersonRequestDTO salePersonRequest, LanguageType languageType) {
        LOGGER.info("Updating sale person: {}",salePersonRequest);
        Long salePersonId = salePersonRequest.getId();
        Optional<SalePerson> optionalSalePerson = salePersonRepository.findById(salePersonId);
        if (optionalSalePerson.isPresent()) {
            SalePerson existingSalePerson = optionalSalePerson.get();
            existingSalePerson.setName(salePersonRequest.getName() == null ? existingSalePerson.getName() : salePersonRequest.getName());
            existingSalePerson.setArabicDescription(salePersonRequest.getArabicDescription() == null ? existingSalePerson.getArabicDescription() : salePersonRequest.getArabicDescription());
            existingSalePerson.setArabicName(salePersonRequest.getArabicName() == null ? existingSalePerson.getArabicName() : salePersonRequest.getArabicName());
            existingSalePerson.setDescription(salePersonRequest.getDescription() == null ? existingSalePerson.getDescription() : salePersonRequest.getDescription());
            existingSalePerson.setEmail(salePersonRequest.getEmail() == null ? existingSalePerson.getEmail() : salePersonRequest.getEmail());
            existingSalePerson.setIsActive(salePersonRequest.getIsActive() == null ? existingSalePerson.getIsActive() : salePersonRequest.getIsActive());
            existingSalePerson.setCity(salePersonRequest.getCityId() == null ? existingSalePerson.getCity() : cityService.findById(salePersonRequest.getCityId(), languageType));

            return save(existingSalePerson);
        } else {
            throw new ResourceNotFoundException("SalePerson not found with ID: " , salePersonId, true);
        }
    }


    @Override
    public void markInactive(Long id) {
        LOGGER.info("Deactivating sale person with id: {}", id);
        SalePerson existingSalePerson = findSalePersonById(id);
        if (existingSalePerson != null) {
            existingSalePerson.setIsActive(false);
            salePersonRepository.save(existingSalePerson);
        }
    }
}


