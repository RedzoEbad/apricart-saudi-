package com.apricart.consumer.service;


import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.enity.SubCategory;
import com.apricart.consumer.security.dto.request.SubCategoryRequestDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.security.enums.LevelType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SubCategoryService {
    List<SubCategory> getAllSubCategories(LanguageType languageType);
    SubCategory findById(Long id, LanguageType languageType);
    SubCategory findByName(String name);
    List<SubCategory> findByCategoryId(Long id, LanguageType languageType, Long warehouseId);
    List<SubCategory> findByLevel(LevelType level);
    List<SubCategory> getActiveSubCategories(LanguageType languageType);
    String getSubCategoryImage(Long id, LanguageType languageType);
    SubCategory updateSubCategoryLevelById(Long id, LevelType level, LanguageType languageType);
    SubCategory updateSubCategoryPosition(Long id, Integer position, LanguageType languageType);
    void addSubCategory(SubCategoryRequestDTO categoryRequestDTO, LanguageType languageType);
    SubCategory updateSubCategory(SubCategoryRequestDTO categoryRequestDTO, LanguageType languageType);
    void deleteSubCategory(Long id, LanguageType languageType);
    ResponseEntity<?> addOrUpdateSubCategoryImage(Long subCategoryId, MultipartFile image, Customer customer, LanguageType lang);

}
