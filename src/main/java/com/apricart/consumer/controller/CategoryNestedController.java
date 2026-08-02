package com.apricart.consumer.controller;

import com.apricart.consumer.enity.SubCategory;
import com.apricart.consumer.generic.GenericResponse;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.security.dto.dto.ProductDetailDTO;
import com.apricart.consumer.security.dto.response.CategoryResponseDTO;
import com.apricart.consumer.security.dto.response.SubCategoryResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.CategoryService;
import com.apricart.consumer.service.SubCategoryService;
import com.apricart.consumer.utils.ProductControllerUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/v1/auth/open/categories-tree")
@Api(value = "Category Tree Controller", tags = {"Category Tree"})
public class CategoryNestedController {
    protected static final Logger LOGGER = LoggerFactory.getLogger(CategoryNestedController.class);

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SubCategoryService subCategoryService;
    @Autowired
    private ProductControllerUtil productControllerUtil;

    private static final Map<String, List<CategoryResponseDTO>> NESTED_CACHE = new ConcurrentHashMap<>();

    @ApiOperation(value = "Get All Categories, SubCategories, and Products in ONE single nested tree API response")
    @GetMapping
    public ResponseEntity<GenericResponse<List<CategoryResponseDTO>>> getAllNestedCategories(
            @RequestParam(value = "warehouseId", required = false, defaultValue = "1") Long warehouseId,
            @RequestParam(required = false) Long customerId,
            @RequestHeader(value = "Language", required = false) LanguageType lang) {

        if (lang == null) {
            lang = LanguageType.ENG;
        }

        String cacheKey = warehouseId + "_" + lang + "_" + (customerId != null ? customerId : 0);
        if (NESTED_CACHE.containsKey(cacheKey)) {
            LOGGER.info("Returning nested categories from in-memory cache for key: {}", cacheKey);
            return Response.success(NESTED_CACHE.get(cacheKey));
        }

        List<CategoryResponseDTO> categories = categoryService.getCategoriesByWarehouseId(warehouseId);
        if (categories != null && !categories.isEmpty()) {
            for (CategoryResponseDTO cat : categories) {
                try {
                    List<SubCategoryResponseDTO> subCats = SubCategory.toDTOList(
                            subCategoryService.findByCategoryId(cat.getId(), lang, warehouseId));
                    if (subCats != null && !subCats.isEmpty()) {
                        for (SubCategoryResponseDTO subCat : subCats) {
                            try {
                                ResponseEntity<GenericResponse<List<ProductDetailDTO>>> response =
                                        productControllerUtil.getProductsBySubCategory(
                                                lang, warehouseId, customerId, subCat.getId(), 0, 50);
                                if (response != null && response.getBody() != null && response.getBody().getData() != null) {
                                    subCat.setProducts(response.getBody().getData());
                                } else {
                                    subCat.setProducts(Collections.emptyList());
                                }
                            } catch (Exception e) {
                                subCat.setProducts(Collections.emptyList());
                            }
                        }
                    } else {
                        subCats = Collections.emptyList();
                    }
                    cat.setSubCategories(subCats);
                } catch (Exception e) {
                    LOGGER.warn("Failed to build subcategories for category {}: {}", cat.getId(), e.getMessage());
                    cat.setSubCategories(Collections.emptyList());
                }
            }
            NESTED_CACHE.put(cacheKey, categories);
        }

        return !categories.isEmpty() ? Response.success(categories) : Response.notFound();
    }
}
