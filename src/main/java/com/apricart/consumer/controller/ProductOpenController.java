package com.apricart.consumer.controller;

import com.apricart.consumer.generic.GenericResponse;
import com.apricart.consumer.security.dto.dto.ProductDetailDTO;
import com.apricart.consumer.security.dto.response.PaginatedResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.utils.ProductControllerUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/auth/open/products")
@Api(value = "Product Controller", tags = {"Product"})
public class ProductOpenController {
    protected static final Logger LOGGER = LoggerFactory.getLogger(ProductOpenController.class);

    @Autowired
    private ProductControllerUtil productControllerUtil;

    @ApiOperation(value = "Get all products (paginated, warehouse-scoped)")
    @GetMapping
    public ResponseEntity<GenericResponse<PaginatedResponseDTO<ProductDetailDTO>>> getAllProducts(
            @RequestHeader(value = "Language", required = false) LanguageType lang,
            @RequestParam Long warehouseId,
            @RequestParam(required = false) Long customerId,
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "20", required = false) int pageSize) {
        if (lang == null) {
            lang = LanguageType.ENG;
        }
        return productControllerUtil.getAllProducts(lang, warehouseId, customerId, pageNo, pageSize);
    }

    @ApiOperation(value = "Get products by Category Id")
    @GetMapping("/category/{id}")
    public ResponseEntity<GenericResponse<List<ProductDetailDTO>>> findProductsByCategoryId(@RequestHeader(value = "Language", required = false) LanguageType lang,
                                                                                            @RequestParam Long warehouseId,
                                                                                            @RequestParam(required = false) Long customerId,
                                                                                            @PathVariable Long id,
                                                                                            @RequestParam(defaultValue = "0", required = false) int pageNo,
                                                                                            @RequestParam(defaultValue = "100", required = false) int pageSize) {
        if (lang == null) {
            lang = LanguageType.ENG;
        }
        return productControllerUtil.getProductsResponseCategory(lang, id, warehouseId, customerId, pageNo, pageSize);
    }

    @ApiOperation(value = "Get products by Sub Category Id")
    @GetMapping("/subcategory/{id}")
    public ResponseEntity<GenericResponse<List<ProductDetailDTO>>> findProductsBySubCategoryId(@RequestHeader(value = "Language", required = false) LanguageType lang,
                                                                                               @RequestParam Long warehouseId,
                                                                                               @RequestParam(required = false) Long customerId,
                                                                                               @PathVariable Long id,
                                                                                               @RequestParam(defaultValue = "0", required = false) int pageNo,
                                                                                               @RequestParam(defaultValue = "10", required = false) int pageSize) {
        if (lang == null) {
            lang = LanguageType.ENG;
        }
        return productControllerUtil.getProductsBySubCategory(lang, warehouseId, customerId, id, pageNo, pageSize);
    }

    @ApiOperation(value = "Get Product Details by Id")
    @GetMapping("/details/{productId}")
    public ResponseEntity<GenericResponse<ProductDetailDTO>> findProductDetailsById(@RequestHeader(value = "Language", required = false) LanguageType lang,
                                                                                    @RequestParam Long warehouseId,
                                                                                    @RequestParam(required = false) Long customerId,
                                                                                    @PathVariable Long productId) {
        if (lang == null) {
            lang = LanguageType.ENG;
        }
        return productControllerUtil.getProductDetails(lang, productId, warehouseId, customerId);
    }

    @ApiOperation(value = "Get similar items")
    @GetMapping("/similar-items")
    public ResponseEntity<GenericResponse<List<ProductDetailDTO>>> getSimilarItems(@RequestHeader(value = "Language", required = false) LanguageType lang,
                                                                                   @RequestParam Long categoryId,
                                                                                   @RequestParam Long subCategoryId,
                                                                                   @RequestParam Long productId,
                                                                                   @RequestParam Long warehouseId,
                                                                                   @RequestParam(required = false) Long customerId,
                                                                                   @RequestParam(defaultValue = "0", required = false) int pageNo,
                                                                                   @RequestParam(defaultValue = "10") int limit) {
        if (lang == null) {
            lang = LanguageType.ENG;
        }
        return productControllerUtil.getSimilarItems(lang, categoryId, subCategoryId, productId, warehouseId, customerId, pageNo, limit);
    }

    @ApiOperation(value = "Get products by Brand Id")
    @GetMapping("/brand/{id}")
    public ResponseEntity<GenericResponse<List<ProductDetailDTO>>> findProductsByBrandId(@RequestHeader(value = "Language", required = false) LanguageType lang,
                                                                                         @RequestParam Long warehouseId,
                                                                                         @RequestParam(required = false) Long customerId,
                                                                                         @PathVariable Long id,
                                                                                         @RequestParam(defaultValue = "0", required = false) int pageNo,
                                                                                         @RequestParam(defaultValue = "200", required = false) int pageSize) {
        if (lang == null) {
            lang = LanguageType.ENG;
        }
        return productControllerUtil.getProductsByBrand(lang, warehouseId, customerId, id, pageNo, pageSize);
    }
}
