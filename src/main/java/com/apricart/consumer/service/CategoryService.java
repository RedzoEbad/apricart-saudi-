package com.apricart.consumer.service;


import com.apricart.consumer.enity.Category;
import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.security.dto.request.CategoryRequestDTO;
import com.apricart.consumer.security.dto.response.CategoryResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.security.enums.LevelType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories(LanguageType lang);
    Category findById(Long id, LanguageType languageType);
    Category findByName(String name);
    Category findByArabicName(String arabicName);
    List<Category> findByLevel(LevelType level);
    List<Category> getActiveCategories(LanguageType lang);
    String getCategoryImage(Long id, LanguageType languageType);
    Category updateCategoryLevelById(Long id, LevelType level, LanguageType languageType);
    Category updateCategoryPosition(Long id, Integer position, LanguageType languageType);
    void addCategory(CategoryRequestDTO categoryRequestDTO, LanguageType languageType);
    Category updateCategory(CategoryRequestDTO categoryRequestDTO, LanguageType languageType);
    void deleteCategory(Long id, LanguageType languageType);
    ResponseEntity<?> addOrUpdateCategoryImage(Long categoryId, MultipartFile image, Customer customer, LanguageType lang);
    List<CategoryResponseDTO> getCategoriesByWarehouseId(Long warehouseId);
    List<Category> getDiscountedCategories(Boolean IsDiscountedCategory);
    Boolean checkIsDiscountedCategory(Long categoryId, LanguageType languageType);
}
