package com.apricart.consumer.controller;

import com.apricart.consumer.enity.City;
import com.apricart.consumer.generic.GenericResponse;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.security.dto.request.CityRequestDTO;
import com.apricart.consumer.security.dto.response.CityResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.BaseService;
import com.apricart.consumer.service.CityService;
import com.apricart.consumer.service.WarehouseService;
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

import static com.apricart.consumer.security.constants.ArabicResponseMessages.CITY_REMOVED_SUCCESSFULLY_ARABIC;
import static com.apricart.consumer.security.constants.ResponseMessage.CITY_REMOVED_SUCCESSFULLY;

@RestController
@RequestMapping("/v1/auth/open/cities")
@Api(value = "City Open Controller", tags = {"City"})
public class CityOpenController {

    @Autowired
    private CityService cityService;
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private BaseService baseService;

    @ApiOperation(value = "Get all cities")
    @GetMapping
    public ResponseEntity<GenericResponse<List<CityResponseDTO>>> getAllCities(@RequestHeader("Language") LanguageType lang) {
        List<CityResponseDTO> cities = City.toDTOList(cityService.getAllCities(), warehouseService, lang);
        return !cities.isEmpty() ? Response.success(cities) : Response.notFound();
    }

    @ApiOperation(value = "Get active cities")
    @GetMapping("/active")
    public ResponseEntity<GenericResponse<List<CityResponseDTO>>> getActiveCities(@RequestHeader("Language") LanguageType lang) {
        List<CityResponseDTO> cities = City.toDTOList(cityService.getActiveCities(), warehouseService, lang);
        return !cities.isEmpty() ? Response.success(cities) : Response.notFound();
    }

    @ApiOperation(value = "Get city by Id")
    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<CityResponseDTO>> findCityById(@PathVariable Long id, @RequestHeader("Language") LanguageType lang) {
        CityResponseDTO city = City.toDTO(cityService.findById(id, lang), warehouseService, lang);
        return city != null ? Response.success(city) : Response.notFound();
    }

    @ApiOperation(value = "Add City")
    @PostMapping
    public ResponseEntity<GenericResponse<CityResponseDTO>> addCity(@Valid @RequestBody CityRequestDTO cityRequestDTO, @RequestHeader("Language") LanguageType lang) {
        cityService.addCity(cityRequestDTO);
        return Response.created();
    }

    @ApiOperation(value = "Update City")
    @PutMapping
    public ResponseEntity<GenericResponse<CityResponseDTO>> updateCity(@Valid @RequestBody CityRequestDTO cityRequestDTO, @RequestHeader("Language") LanguageType lang) {
        CityResponseDTO updatedCity = City.toDTO(cityService.updateCity(cityRequestDTO, lang), warehouseService, lang);
        return updatedCity != null ? Response.success(updatedCity) : Response.notFound();
    }

    @ApiOperation(value = "City Image Update By Id", authorizations = {@Authorization(value = "jwtToken")})
    @PostMapping("/image/update/{id}")
    public ResponseEntity<?> updateCityImage(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable Long id,
            @RequestParam(value = "image", required = true) MultipartFile image,
            HttpServletRequest request,
            @RequestHeader("Language") LanguageType lang) {
        return cityService.addOrUpdateCityImage(id, image, baseService.resolveUser(request), lang);
    }

    @ApiOperation(value = "Get city image by Id", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping("/image/{id}")
    public ResponseEntity<GenericResponse<String>> getCityImageById(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable Long id,
            @RequestHeader("Language") LanguageType lang) {
        String categoryImage = cityService.getCityImage(id, lang);
        return !categoryImage.isEmpty() ? Response.success(categoryImage) : Response.notFound();
    }
}
