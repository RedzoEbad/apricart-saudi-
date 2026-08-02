package com.apricart.consumer.controller;

import com.apricart.consumer.enity.SubCategory;
import com.apricart.consumer.generic.GenericResponse;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.security.dto.dto.ProductDetailDTO;
import com.apricart.consumer.security.dto.response.CategoryDetailsDTO;
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

@RestController
@RequestMapping("/v1/auth/open/categories")
@Api(value = "Category Controller", tags = {"Category"})
public class CategoryOpenController {
    protected static final Logger LOGGER = LoggerFactory.getLogger(CategoryOpenController.class);

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SubCategoryService subCategoryService;
    @Autowired
    private ProductControllerUtil productControllerUtil;

    @ApiOperation(value = "Get Categories by Warehouse Id")
    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<List<CategoryResponseDTO>>> findCategoryByWarehouseId(
            @PathVariable Long id,
            @RequestHeader("Language") LanguageType lang) {
        List<CategoryResponseDTO> categories = categoryService.getCategoriesByWarehouseId(id);
        return !categories.isEmpty() ? Response.success(categories) : Response.notFound();
    }

    @ApiOperation(value = "Get Sub Categories by Category Id")
    @GetMapping("/{id}/subcategories")
    public ResponseEntity<GenericResponse<List<SubCategoryResponseDTO>>> findSubCategoryByCategoryId(
            @PathVariable Long id,
            @RequestParam("warehouseId") Long warehouseId,
            @RequestHeader("Language") LanguageType lang) {

        List<SubCategoryResponseDTO> subCategories = SubCategory.toDTOList(subCategoryService.findByCategoryId(id, lang, warehouseId));
        return !subCategories.isEmpty() ? Response.success(subCategories) : Response.notFound();
    }

    @ApiOperation(value = "Get Sub Categories and Initial SubCategory Products in a single call")
    @GetMapping("/{id}/details")
    public ResponseEntity<GenericResponse<CategoryDetailsDTO>> getCategoryDetails(
            @PathVariable Long id,
            @RequestParam("warehouseId") Long warehouseId,
            @RequestParam(required = false) Long customerId,
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "50", required = false) int pageSize,
            @RequestHeader("Language") LanguageType lang) {

        List<SubCategoryResponseDTO> subCategories = SubCategory.toDTOList(subCategoryService.findByCategoryId(id, lang, warehouseId));
        List<ProductDetailDTO> firstSubCatProducts = Collections.emptyList();

        if (subCategories != null && !subCategories.isEmpty()) {
            for (SubCategoryResponseDTO subCat : subCategories) {
                try {
                    ResponseEntity<GenericResponse<List<ProductDetailDTO>>> response = productControllerUtil.getProductsBySubCategory(
                            lang, warehouseId, customerId, subCat.getId(), pageNo, pageSize);
                    if (response != null && response.getBody() != null && response.getBody().getData() != null) {
                        subCat.setProducts(response.getBody().getData());
                    } else {
                        subCat.setProducts(Collections.emptyList());
                    }
                } catch (Exception e) {
                    LOGGER.warn("Failed to fetch products for subcategory {}: {}", subCat.getId(), e.getMessage());
                    subCat.setProducts(Collections.emptyList());
                }
            }
            if (subCategories.get(0).getProducts() != null) {
                firstSubCatProducts = subCategories.get(0).getProducts();
            }
        }

        CategoryDetailsDTO detailsDTO = CategoryDetailsDTO.builder()
                .subCategories(subCategories)
                .products(firstSubCatProducts)
                .build();

        return Response.success(detailsDTO);
    }
}
