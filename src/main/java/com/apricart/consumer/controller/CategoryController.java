package com.apricart.consumer.controller;

import com.apricart.consumer.enity.Category;
import com.apricart.consumer.generic.GenericResponse;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.security.dto.request.CategoryRequestDTO;
import com.apricart.consumer.security.dto.response.CategoryResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.security.enums.LevelType;
import com.apricart.consumer.service.BaseService;
import com.apricart.consumer.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.CATEGORY_REMOVED_SUCCESSFULLY_ARABIC;
import static com.apricart.consumer.security.constants.ResponseMessage.CATEGORY_REMOVED_SUCCESSFULLY;

@RestController
@RequestMapping("/v1/categories")
@Api(value = "Category Controller", tags = {"Category"})
public class CategoryController {
    protected static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;
    @Autowired
    BaseService baseService;

    @ApiOperation(value = "Get all categories", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping
    public ResponseEntity<GenericResponse<List<CategoryResponseDTO>>> getAllCategories(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestHeader("Language") LanguageType lang) {
        List<CategoryResponseDTO> categories = Category.toDTOList(categoryService.getAllCategories(lang));
        return !categories.isEmpty() ? Response.success(categories) : Response.notFound();
    }

    @ApiOperation(value = "Get category by Name", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping("/name/{name}")
    public ResponseEntity<GenericResponse<CategoryResponseDTO>> findCategoryByName(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable String name,
            @RequestHeader("Language") LanguageType lang) {
        Category category = categoryService.findByName(name);
        return category != null ? Response.success(Category.toDTO(category)) : Response.notFound();
    }

    @ApiOperation(value = "Get category by Arabic Name", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping("/arabic/{arabicName}")
    public ResponseEntity<GenericResponse<CategoryResponseDTO>> findCategoryByArabicName(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable String arabicName,
            @RequestHeader("Language") LanguageType lang) {
        Category category = categoryService.findByArabicName(arabicName);
        return category != null ? Response.success(Category.toDTO(category)) : Response.notFound();
    }

    @ApiOperation(value = "Get category by Level", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping("/level/{level}")
    public ResponseEntity<GenericResponse<List<CategoryResponseDTO>>> findCategoryByLevel(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable LevelType level,
            @RequestHeader("Language") LanguageType lang) {
        List<CategoryResponseDTO> categories = Category.toDTOList(categoryService.findByLevel(level));
        return !categories.isEmpty() ? Response.success(categories) : Response.notFound();
    }

    @ApiOperation(value = "Get category by Id", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<CategoryResponseDTO>> findCategoryById(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable Long id,
            @RequestHeader("Language") LanguageType lang) {
        Category category = categoryService.findById(id, lang);
        return category != null ? Response.success(Category.toDTO(category)) : Response.notFound();
    }

    @ApiOperation(value = "Get all active categories", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping("/active")
    public ResponseEntity<GenericResponse<List<CategoryResponseDTO>>> getActiveCategories(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestHeader("Language") LanguageType lang) {
        List<CategoryResponseDTO> categoryList = Category.toDTOList(categoryService.getActiveCategories(lang));
        return !categoryList.isEmpty() ? Response.success(categoryList) : Response.notFound();
    }

    @ApiOperation(value = "Add Category", authorizations = {@Authorization(value = "jwtToken")})
    @PostMapping
    public ResponseEntity<GenericResponse<CategoryResponseDTO>> addCategory(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @Valid @RequestBody CategoryRequestDTO categoryRequestDTO,
            @RequestHeader("Language") LanguageType lang) {
        categoryService.addCategory(categoryRequestDTO, lang);
        return Response.created();
    }

    @ApiOperation(value = "Update Category", authorizations = {@Authorization(value = "jwtToken")})
    @PutMapping
    public ResponseEntity<GenericResponse<CategoryResponseDTO>> updateCategory(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @Valid @RequestBody CategoryRequestDTO categoryRequestDTO,
            @RequestHeader("Language") LanguageType lang) {
        Category updatedCategory = categoryService.updateCategory(categoryRequestDTO, lang);
        return updatedCategory != null ? Response.success(Category.toDTO(updatedCategory)) : Response.notFound();
    }

    @ApiOperation(value = "Update Category Level By Id", authorizations = {@Authorization(value = "jwtToken")})
    @PutMapping("/{id}/{level}")
    public ResponseEntity<GenericResponse<CategoryResponseDTO>> updateCategoryLevelById(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable Long id,
            @PathVariable LevelType level,
            @RequestHeader("Language") LanguageType lang) {
        Category updatedCategory = categoryService.updateCategoryLevelById(id, level, lang);
        return updatedCategory != null ? Response.success(Category.toDTO(updatedCategory)) : Response.notFound();
    }

    @ApiOperation(value = "Update Category Position", authorizations = {@Authorization(value = "jwtToken")})
    @PutMapping("/position/{id}/{position}")
    public ResponseEntity<GenericResponse<CategoryResponseDTO>> updateCategoryPosition(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable Long id,
            @PathVariable Integer position,
            @RequestHeader("Language") LanguageType lang) {
        Category updatedCategory = categoryService.updateCategoryPosition(id, position, lang);
        return updatedCategory != null ? Response.success(Category.toDTO(updatedCategory)) : Response.notFound();
    }

    @ApiOperation(value = "Delete Category By Id", authorizations = {@Authorization(value = "jwtToken")})
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<String>> deleteCategory(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable Long id,
            @RequestHeader("Language") LanguageType lang) {
        categoryService.deleteCategory(id, lang);
        return lang.equals(LanguageType.ARB) ? Response.success(CATEGORY_REMOVED_SUCCESSFULLY_ARABIC) : Response.success(CATEGORY_REMOVED_SUCCESSFULLY);
    }

    @ApiOperation(value = "Get category image by Id", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping("/image/{id}")
    public ResponseEntity<GenericResponse<String>> getCategoryImageById(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable Long id,
            @RequestHeader("Language") LanguageType lang) {
        String categoryImage = categoryService.getCategoryImage(id, lang);
        return !categoryImage.isEmpty() ? Response.success(categoryImage) : Response.notFound();
    }

    @ApiOperation(value = "Category Image Update By Id", authorizations = {@Authorization(value = "jwtToken")})
    @PostMapping("/image/update/{id}")
    public ResponseEntity<?> updateCategoryImage(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable Long id,
            @RequestParam(value = "image", required = true) MultipartFile image,
            HttpServletRequest request,
            @RequestHeader("Language") LanguageType lang) {
        LOGGER.info("Image: " + image.getSize());
        return categoryService.addOrUpdateCategoryImage(id, image, baseService.resolveUser(request), lang);
    }

}

