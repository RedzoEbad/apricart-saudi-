package com.apricart.consumer.service;

import com.apricart.consumer.enity.Product;
import com.apricart.consumer.security.dto.request.ProductRequestDTO;
import com.apricart.consumer.security.enums.LanguageType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    void addProduct(ProductRequestDTO productRequestDTO, LanguageType languageType);
    Product updateProduct(ProductRequestDTO productRequestDTO, LanguageType languageType);
    void deleteProduct(Long id, LanguageType languageType);

    Page<Product> findAll(PageRequest pageRequest);

    List<Product> findAllProducts(LanguageType lang, int page, int size);
    Product findById(Long id, LanguageType languageType);
    Product findProductBySKU(String sku);
    List<Product> findByCategoryId(Long id, int page, int size, LanguageType languageType);
    List<Product> findBySubCategoryId(Long id, int page, int size, LanguageType languageType);
    List<Product> findByBrandId(Long id, int page, int size, LanguageType languageType);
    List<Product> findByZohoId(Long id, int page, int size);
    List<Product> getTrendingProducts(LanguageType lang, int page, int size);
    List<Product> getDiscountedProducts(LanguageType lang, int pageNo, int pageSize);
    List<Product> getFeaturedProducts(LanguageType lang, int page, int size);
    List<Product> getNewArrivalsProducts(LanguageType lang, int page, int size);
    Product updateProductStatusById(Long id, Boolean status, LanguageType languageType);
    Product updateProductPosition(Long id, Integer position, LanguageType languageType);
    String getProductImage(Long id, LanguageType languageType);
    ResponseEntity<?> addOrUpdateProductImage(Long productId, MultipartFile image, LanguageType lang);
}
