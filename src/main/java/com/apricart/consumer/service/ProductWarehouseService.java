package com.apricart.consumer.service;

import com.apricart.consumer.enity.Category;
import com.apricart.consumer.enity.ProductWarehouse;
import com.apricart.consumer.security.dto.request.ProductWarehouseRequestDTO;
import com.apricart.consumer.security.dto.response.ProductWarehouseResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;

import java.util.List;

public interface ProductWarehouseService {
    List<ProductWarehouse> getAllProductsWarehouse();
    ProductWarehouse findById(Long id, LanguageType languageType);
    ProductWarehouseResponseDTO findByProductId(Long id, LanguageType languageType);
    ProductWarehouseResponseDTO findByProductIdAndWarehouseId(Long productId, Long warehouseId, LanguageType languageType);
    ProductWarehouse findProductWarehouseByProductId(Long id, LanguageType languageType);
    List<ProductWarehouse> findByWarehouseId(Long id, LanguageType languageType);
    List<Category> findCategoriesByWarehouseId(Long warehouseId);
    List<ProductWarehouse> findByPriceListId(Long id, LanguageType languageType);
    ProductWarehouse updateProductWarehouseStatusById(Long id, Boolean status, LanguageType languageType);
    void addProductWarehouse(ProductWarehouseRequestDTO productWarehouseRequestDTO, LanguageType lang);
    ProductWarehouse updateProductWarehouse(ProductWarehouseRequestDTO productWarehouseRequestDTO, LanguageType languageType);
    void deleteProductWarehouse(Long id, LanguageType languageType);
    List<ProductWarehouseResponseDTO> findByCategoryIdAndWarehouseId(Long id, Long warehouseId, int page, int size, LanguageType languageType);
    List<ProductWarehouseResponseDTO> findBySubCategoryIdAndWarehouseId(Long id, Long warehouseId, int page, int size, LanguageType languageType);
    List<ProductWarehouseResponseDTO> findByBrandIdAndWarehouseId(Long brandId, Long warehouseId, int page, int size, LanguageType languageType);
    List<ProductWarehouseResponseDTO> findNewArrivalsByWarehouseId(Long warehouseId, int pageNo, int pageSize, LanguageType languageType);
    List<ProductWarehouseResponseDTO> findTrendingByWarehouseId(Long warehouseId, int pageNo, int pageSize, LanguageType languageType);
    List<ProductWarehouseResponseDTO> findRecommendedByWarehouseId(Long warehouseId, int pageNo, int pageSize, LanguageType languageType);
    List<ProductWarehouseResponseDTO> findSimilarItems(Long categoryId, Long subCategoryId,
                                                       Long productId, Long warehouseId, int pageNo, int limit, LanguageType languageType);
    ProductWarehouse updateQuantityByWarehouseAndSku(String sku, Long warehouseId, int quantity, LanguageType languageType);


}
