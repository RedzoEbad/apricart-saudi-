package com.apricart.consumer.service;

import com.apricart.consumer.enity.City;
import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.security.dto.request.CityRequestDTO;
import com.apricart.consumer.security.enums.LanguageType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CityService {

    List<City> getAllCities();
    List<City> getActiveCities();
    City findById(Long id, LanguageType languageType);
    void addCity(CityRequestDTO cityRequestDTO);
    City updateCity(CityRequestDTO cityRequestDTO, LanguageType languageType);
    void deleteCity(Long id, LanguageType languageType);
    String getCityImage(Long id, LanguageType languageType);
    ResponseEntity<?> addOrUpdateCityImage(Long cityId, MultipartFile image, Customer customer, LanguageType lang);

}
