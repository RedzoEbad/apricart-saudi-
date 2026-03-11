package com.apricart.consumer.controller;

import com.apricart.consumer.enity.SubCategory;
import com.apricart.consumer.generic.GenericResponse;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.security.dto.request.SubCategoryRequestDTO;
import com.apricart.consumer.security.dto.response.SubCategoryResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.security.enums.LevelType;
import com.apricart.consumer.service.BaseService;
import com.apricart.consumer.service.SubCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.SUB_CATEGORY_REMOVED_SUCCESSFULLY_ARABIC;
import static com.apricart.consumer.security.constants.ResponseMessage.SUB_CATEGORY_REMOVED_SUCCESSFULLY;

@RestController
@RequestMapping("/v1/subcategories")
@Api(value = "SubCategory Controller", tags = {"SubCategory"})
public class SubCategoryController{

    @Autowired
    private SubCategoryService subCategoryService;
    @Autowired
    BaseService baseService;

    @ApiOperation(value = "Get all sub categories", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping
    public ResponseEntity<GenericResponse<List<SubCategoryResponseDTO>>> getAllSubCategories(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestHeader("Language") LanguageType lang) {
        List<SubCategoryResponseDTO> subcategories = SubCategory.toDTOList(subCategoryService.getAllSubCategories(lang));
        return !subcategories.isEmpty() ? Response.success(subcategories) : Response.notFound();
    }

    @ApiOperation(value = "Get sub category by Name", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping("/name/{name}")
    public ResponseEntity<GenericResponse<SubCategoryResponseDTO>> findSubCategoryByName(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestHeader("Language") LanguageType lang,
            @PathVariable String name) {
        SubCategory subcategory = subCategoryService.findByName(name);
        return subcategory != null ? Response.success(SubCategory.toDTO(subcategory)) : Response.notFound();
    }

    @ApiOperation(value = "Get sub category by Level", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping("/level/{level}")
    public ResponseEntity<GenericResponse<List<SubCategoryResponseDTO>>> findSubCategoryByLevel(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestHeader("Language") LanguageType lang,
            @PathVariable LevelType level) {
        List<SubCategoryResponseDTO> subcategories = SubCategory.toDTOList(subCategoryService.findByLevel(level));
        return !subcategories.isEmpty() ? Response.success(subcategories) : Response.notFound();
    }

    @ApiOperation(value = "Get sub category by Id", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<SubCategoryResponseDTO>> findSubCategoryById(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestHeader("Language") LanguageType lang,
            @PathVariable Long id) {
        SubCategory subcategory = subCategoryService.findById(id, lang);
        return subcategory != null ? Response.success(SubCategory.toDTO(subcategory)) : Response.notFound();
    }

    @ApiOperation(value = "Get sub category by category Id", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping("/category/{id}")
    public ResponseEntity<GenericResponse<List<SubCategoryResponseDTO>>> findSubCategoryByCategoryId(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestHeader("Language") LanguageType lang,
            @PathVariable Long id,
            @RequestParam ("warehouseId") Long warehouseId) {
        List<SubCategoryResponseDTO> subcategories = SubCategory.toDTOList(subCategoryService.findByCategoryId(id, lang, warehouseId));
        return !subcategories.isEmpty() ? Response.success(subcategories) : Response.notFound();
    }

    @ApiOperation(value = "Get all active sub categories", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping("/active")
    public ResponseEntity<GenericResponse<List<SubCategoryResponseDTO>>> getActiveSubCategories(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestHeader("Language") LanguageType lang) {
        List<SubCategoryResponseDTO> subcategoryList = SubCategory.toDTOList(subCategoryService.getActiveSubCategories(lang));
        return !subcategoryList.isEmpty() ? Response.success(subcategoryList) : Response.notFound();
    }

    @ApiOperation(value = "Add Sub Category", authorizations = { @Authorization(value="jwtToken") })
    @PostMapping
    public ResponseEntity<GenericResponse<SubCategoryResponseDTO>> addSubCategory(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestHeader("Language") LanguageType lang,
            @Valid @RequestBody SubCategoryRequestDTO subcategoryRequestDTO) {
        subCategoryService.addSubCategory(subcategoryRequestDTO, lang);
        return Response.created();
    }

    @ApiOperation(value = "Update Sub Category", authorizations = { @Authorization(value="jwtToken") })
    @PutMapping
    public ResponseEntity<GenericResponse<SubCategoryResponseDTO>> updateSubCategory(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestHeader("Language") LanguageType lang,
            @Valid @RequestBody SubCategoryRequestDTO subcategoryRequestDTO) {
        SubCategory updatedSubCategory = subCategoryService.updateSubCategory(subcategoryRequestDTO, lang);
        return updatedSubCategory != null ? Response.success(SubCategory.toDTO(updatedSubCategory)) : Response.notFound();
    }

    @ApiOperation(value = "Update Sub Category Level By Id", authorizations = { @Authorization(value="jwtToken") })
    @PutMapping("/{id}/{level}")
    public ResponseEntity<GenericResponse<SubCategoryResponseDTO>> updateSubCategoryLevelById(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestHeader("Language") LanguageType lang,
            @PathVariable Long id,
            @PathVariable LevelType level) {
        SubCategory updatedSubCategory = subCategoryService.updateSubCategoryLevelById(id, level, lang);
        return updatedSubCategory != null ? Response.success(SubCategory.toDTO(updatedSubCategory)) : Response.notFound();
    }

    @ApiOperation(value = "Update Sub Category Position By Id", authorizations = { @Authorization(value="jwtToken") })
    @PutMapping("/position/{id}/{position}")
    public ResponseEntity<GenericResponse<SubCategoryResponseDTO>> updateSubCategoryPosition(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestHeader("Language") LanguageType lang,
            @PathVariable Long id,
            @PathVariable Integer position) {
        SubCategory updatedSubCategory = subCategoryService.updateSubCategoryPosition(id, position, lang);
        return updatedSubCategory != null ? Response.success(SubCategory.toDTO(updatedSubCategory)) : Response.notFound();
    }

    @ApiOperation(value = "Delete Sub Category By Id", authorizations = { @Authorization(value="jwtToken") })
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<String>> deleteSubCategory(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestHeader("Language") LanguageType lang,
            @PathVariable Long id) {
        subCategoryService.deleteSubCategory(id, lang);
        return lang.equals(LanguageType.ARB) ? Response.success(SUB_CATEGORY_REMOVED_SUCCESSFULLY_ARABIC) : Response.success(SUB_CATEGORY_REMOVED_SUCCESSFULLY);
    }

    @ApiOperation(value = "Get Sub Category Image by Id", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping("/image/{id}")
    public ResponseEntity<GenericResponse<String>> getCategoryImageById(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestHeader("Language") LanguageType lang,
            @PathVariable Long id) {
        String categoryImage = subCategoryService.getSubCategoryImage(id, lang);
        return !categoryImage.isEmpty() ? Response.success(categoryImage) : Response.notFound();
    }

    @ApiOperation(value = "Sub Category Image Update By Id", authorizations = { @Authorization(value="jwtToken") })
    @PostMapping("/image/update/{id}")
    public ResponseEntity<?> updateCategoryImage(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable Long id,
            @RequestParam(value = "image", required = true) MultipartFile image,
            HttpServletRequest request,
            @RequestHeader("Language") LanguageType lang) {
        return subCategoryService.addOrUpdateSubCategoryImage(id, image,  baseService.resolveUser(request), lang);
    }


}
