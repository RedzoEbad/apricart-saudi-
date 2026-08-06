package com.apricart.consumer.utils;

import com.apricart.consumer.enity.Product;
import com.apricart.consumer.enity.SubCategory;
import com.apricart.consumer.generic.GenericResponse;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.mapper.ProductMapper;
import com.apricart.consumer.security.dto.dto.ProductDetailDTO;
import com.apricart.consumer.security.dto.response.PaginatedResponseDTO;
import com.apricart.consumer.security.dto.response.ProductResponseDTO;
import com.apricart.consumer.security.dto.response.ProductWarehouseResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.CategoryService;
import com.apricart.consumer.service.ProductService;
import com.apricart.consumer.service.ProductWarehouseService;
import com.apricart.consumer.service.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductControllerUtil {

    @Autowired
    private ProductWarehouseService productWarehouseService;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SubCategoryService subCategoryService;

    @Autowired
    private ProductService productService;

    public boolean isDiscountedCategory(Long categoryId, LanguageType languageType) {
        return categoryService.checkIsDiscountedCategory(categoryId, languageType);
    }

    public ResponseEntity<GenericResponse<List<ProductDetailDTO>>> getDiscountedProductsResponse(LanguageType lang, Integer pageNo, Integer pageSize, Long customerId) {
        List<ProductResponseDTO> discountedProducts = Product.toDTOList(productService.getDiscountedProducts(lang, pageNo, pageSize));
        return getProductDetailResponse(discountedProducts, customerId, lang);
    }

    public ResponseEntity<GenericResponse<List<ProductDetailDTO>>> getProductDetailResponse(List<ProductResponseDTO> products, Long customerId, LanguageType lang) {
        List<ProductWarehouseResponseDTO> productsDetails = products.stream()
                .map(product -> productWarehouseService.findByProductId(product.getId(), lang))
                .collect(Collectors.toList());
        List<ProductDetailDTO> productDetails = productMapper.mapAndSortProductDetails(productsDetails, customerId, lang);
        return productDetails.isEmpty() ? Response.notFound() : Response.success(productDetails);
    }

    public ResponseEntity<GenericResponse<List<ProductDetailDTO>>> getProductsResponseCategory(LanguageType lang, Long categoryId, Long warehouseId, Long customerId, int pageNo, int pageSize) {
        List<ProductWarehouseResponseDTO> productsDetails = productWarehouseService.findByCategoryIdAndWarehouseId(categoryId, warehouseId, pageNo, pageSize, lang);
        List<ProductDetailDTO> products = productMapper.mapAndSortProductDetails(productsDetails, customerId, lang);
        return products.isEmpty() ? Response.notFound() : Response.success(products);
    }

    public ResponseEntity<GenericResponse<List<ProductDetailDTO>>> getProductsResponseSubCategory(LanguageType lang, Long subCategoryId, Long warehouseId, Long customerId, int pageNo, int pageSize) {
        List<ProductWarehouseResponseDTO> productsDetails = productWarehouseService.findBySubCategoryIdAndWarehouseId(subCategoryId, warehouseId, pageNo, pageSize, lang);
        List<ProductDetailDTO> products = productMapper.mapAndSortProductDetails(productsDetails, customerId, lang);
        return !products.isEmpty() ? Response.success(products) : Response.notFound();
    }

    public ResponseEntity<GenericResponse<ProductDetailDTO>> getProductDetails(LanguageType lang, Long productId, Long warehouseId, Long customerId) {
        ProductWarehouseResponseDTO productDetail = productWarehouseService.findByProductIdAndWarehouseId(productId, warehouseId, lang);
        ProductDetailDTO product = productMapper.mapAndSortProductDetails(Collections.singletonList(productDetail), customerId, lang).get(0);
        return product != null ? Response.success(product) : Response.notFound();
    }

    public ResponseEntity<GenericResponse<List<ProductDetailDTO>>> getSimilarItems(LanguageType lang, Long categoryId, Long subCategoryId, Long productId, Long warehouseId, Long customerId, int pageNo, int limit) {
        List<ProductWarehouseResponseDTO> productsDetails = productWarehouseService.findSimilarItems(categoryId, subCategoryId, productId, warehouseId, pageNo, limit, lang);
        List<ProductDetailDTO> products = productMapper.mapAndSortProductDetails(productsDetails, customerId, lang);
        return !products.isEmpty() ? Response.success(products) : Response.notFound();
    }

    public ResponseEntity<GenericResponse<List<ProductDetailDTO>>> getProductsBySubCategory(LanguageType lang, Long warehouseId, Long customerId, Long id, int pageNo, int pageSize) {
        return getProductsResponseSubCategory(lang, id, warehouseId, customerId, pageNo, pageSize);
    }

    public ResponseEntity<GenericResponse<List<ProductDetailDTO>>> getProductsByBrand(LanguageType lang, Long warehouseId, Long customerId, Long brandId, int pageNo, int pageSize) {
        List<ProductWarehouseResponseDTO> productsDetails = productWarehouseService.findByBrandIdAndWarehouseId(brandId, warehouseId, pageNo, pageSize, lang);
        List<ProductDetailDTO> products = productMapper.mapAndSortProductDetails(productsDetails, customerId, lang);
        return !products.isEmpty() ? Response.success(products) : Response.notFound();
    }

    public ResponseEntity<GenericResponse<PaginatedResponseDTO<ProductDetailDTO>>> getAllProducts(
            LanguageType lang, Long warehouseId, Long customerId, int pageNo, int pageSize) {
        org.springframework.data.domain.Page<ProductWarehouseResponseDTO> page =
                productWarehouseService.findAllByWarehouseId(warehouseId, pageNo, pageSize, lang);
        List<ProductDetailDTO> products = productMapper.mapAndSortProductDetails(page.getContent(), customerId, lang);
        PaginatedResponseDTO<ProductDetailDTO> payload = PaginatedResponseDTO.<ProductDetailDTO>builder()
                .content(products)
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
        return Response.success(payload);
    }

    public ResponseEntity<GenericResponse<List<ProductDetailDTO>>> getProductDetailResponse(List<ProductResponseDTO> products, Long customerId, LanguageType lang, Long warehouseId) {
        List<ProductWarehouseResponseDTO> productsDetails = products.stream()
                .map(product -> productWarehouseService.findByProductId(product.getId(), lang))
                .filter(pw -> pw.getWarehouseId().equals(warehouseId))
                .collect(Collectors.toList());
        List<ProductDetailDTO> productDetails = productMapper.mapAndSortProductDetails(productsDetails, customerId, lang);
        return productDetails.isEmpty() ? Response.notFound() : Response.success(productDetails);
    }
}

