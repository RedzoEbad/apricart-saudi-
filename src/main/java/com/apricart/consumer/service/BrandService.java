package com.apricart.consumer.service;

import com.apricart.consumer.enity.Brand;
import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.security.dto.request.BrandRequestDTO;
import com.apricart.consumer.security.enums.LanguageType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BrandService {
    List<Brand> getAllBrands(LanguageType lang);
    Brand findById(Long id, LanguageType languageType);
    Brand findByName(String name);
    Brand findByArabicName(String arabicName);
    List<Brand> getActiveBrands(LanguageType lang);
    String getBrandImage(Long id, LanguageType languageType);
    void addBrand(BrandRequestDTO brandRequestDTO);
    Brand updateBrand(BrandRequestDTO brandRequestDTO, LanguageType languageType);
    void deleteBrand(Long id, LanguageType languageType);
    ResponseEntity<?> addOrUpdateBrandImage(Long brandId, MultipartFile image, Customer customer, LanguageType lang);
}
