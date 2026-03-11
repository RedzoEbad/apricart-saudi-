package com.apricart.consumer.controller;

import com.apricart.consumer.enity.Brand;
import com.apricart.consumer.generic.GenericResponse;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.security.dto.request.BrandRequestDTO;
import com.apricart.consumer.security.dto.response.BrandResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.BaseService;
import com.apricart.consumer.service.BrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.BRAND_REMOVED_SUCCESSFULLY_ARABIC;
import static com.apricart.consumer.security.constants.ResponseMessage.BRAND_REMOVED_SUCCESSFULLY;

@RestController
@RequestMapping("/v1/auth/open/brands")
@Api(value = "Brand Controller", tags = {"Brand"})
public class BrandController{

    @Autowired
    private BrandService brandService;
    @Autowired
    BaseService baseService;

    @ApiOperation(value = "Get all Brands")
    @GetMapping
    public ResponseEntity<GenericResponse<List<BrandResponseDTO>>> getAllBrands(
            @RequestHeader("Language") LanguageType lang) {
        List<BrandResponseDTO> brands = Brand.toDTOList(brandService.getAllBrands(lang));
        return !brands.isEmpty() ? Response.success(brands) : Response.notFound();
    }

    @ApiOperation(value = "Get Brand by Name")
    @GetMapping("/name/{name}")
    public ResponseEntity<GenericResponse<BrandResponseDTO>> findBrandByName(
            @PathVariable String name,
            @RequestHeader("Language") LanguageType lang) {
        Brand brand = brandService.findByName(name);
        return brand != null ? Response.success(Brand.toDTO(brand)) : Response.notFound();
    }

    @ApiOperation(value = "Get Brand by Arabic Name")
    @GetMapping("/arabic/{arabicName}")
    public ResponseEntity<GenericResponse<BrandResponseDTO>> findBrandByArabicName(
            @PathVariable String arabicName,
            @RequestHeader("Language") LanguageType lang) {
        Brand brand = brandService.findByArabicName(arabicName);
        return brand != null ? Response.success(Brand.toDTO(brand)) : Response.notFound();
    }

    @ApiOperation(value = "Get Brand by Id")
    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<BrandResponseDTO>> findBrandById(
            @PathVariable Long id,
            @RequestHeader("Language") LanguageType lang) {
        Brand brand = brandService.findById(id, lang);
        return brand != null ? Response.success(Brand.toDTO(brand)) : Response.notFound();
    }

    @ApiOperation(value = "Get all active brands")
    @GetMapping("/active")
    public ResponseEntity<GenericResponse<List<BrandResponseDTO>>> getActiveBrands(
            @RequestHeader("Language") LanguageType lang) {
        List<BrandResponseDTO> brandList = Brand.toDTOList(brandService.getActiveBrands(lang));
        return !brandList.isEmpty() ? Response.success(brandList) : Response.notFound();
    }

    @ApiOperation(value = "Add Brand")
    @PostMapping
    public ResponseEntity<GenericResponse<BrandResponseDTO>> addBrand(
            @Valid @RequestBody BrandRequestDTO brandRequestDTO,
            @RequestHeader("Language") LanguageType lang) {
        brandService.addBrand(brandRequestDTO);
        return Response.created();
    }

    @ApiOperation(value = "Update Brand")
    @PutMapping
    public ResponseEntity<GenericResponse<BrandResponseDTO>> updateBrand(
            @Valid @RequestBody BrandRequestDTO brandRequestDTO,
            @RequestHeader("Language") LanguageType lang) {
        Brand updateBrand = brandService.updateBrand(brandRequestDTO, lang);
        return updateBrand != null ? Response.success(Brand.toDTO(updateBrand)) : Response.notFound();
    }

    @ApiOperation(value = "Delete Brand By Id")
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<String>> deleteBrand(
            @PathVariable Long id,
            @RequestHeader("Language") LanguageType lang) {
        brandService.deleteBrand(id, lang);
        return lang.equals(LanguageType.ARB) ? Response.success(BRAND_REMOVED_SUCCESSFULLY_ARABIC) : Response.success(BRAND_REMOVED_SUCCESSFULLY);
    }

    @ApiOperation(value = "Get brand image by Id")
    @GetMapping("/image/{id}")
    public ResponseEntity<GenericResponse<String>> getBrandImageById(
            @PathVariable Long id,
            @RequestHeader("Language") LanguageType lang) {
        String categoryImage = brandService.getBrandImage(id, lang);
        return !categoryImage.isEmpty() ? Response.success(categoryImage) : Response.notFound();
    }

    @ApiOperation(value = "Brand Image Update By Id")
    @PostMapping("/image/update/{id}")
    public ResponseEntity<?> updateBrandImage(
            @PathVariable Long id,
            @RequestParam(value = "image", required = true) MultipartFile image,
            HttpServletRequest request,
            @RequestHeader("Language") LanguageType lang) {
        return brandService.addOrUpdateBrandImage(id, image,  baseService.resolveUser(request), lang);
    }

}
