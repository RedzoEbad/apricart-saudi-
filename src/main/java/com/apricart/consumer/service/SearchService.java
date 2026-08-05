package com.apricart.consumer.service;

import com.apricart.consumer.enity.ProductWarehouse;
import com.apricart.consumer.security.dto.dto.ProductDetailDTO;
import com.apricart.consumer.security.enums.LanguageType;

import java.util.List;

public interface SearchService {

    List<ProductWarehouse> searchProduct(String query, Long warehouseId);

    List<ProductDetailDTO> searchProductDetails(String query, Long warehouseId, Long customerId, LanguageType lang);
}


