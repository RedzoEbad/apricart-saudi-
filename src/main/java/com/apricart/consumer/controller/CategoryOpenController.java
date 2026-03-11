package com.apricart.consumer.controller;

import com.apricart.consumer.enity.SubCategory;
import com.apricart.consumer.generic.GenericResponse;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.security.dto.response.CategoryResponseDTO;
import com.apricart.consumer.security.dto.response.SubCategoryResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.CategoryService;
import com.apricart.consumer.service.SubCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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




}

