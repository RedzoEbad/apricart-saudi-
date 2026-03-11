package com.apricart.consumer.controller;


import com.apricart.consumer.generic.GenericResponse;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.CityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.CITY_REMOVED_SUCCESSFULLY_ARABIC;
import static com.apricart.consumer.security.constants.ResponseMessage.CITY_REMOVED_SUCCESSFULLY;

@RestController
@RequestMapping("/v1/cities")
@Api(value = "City Controller", tags = {"City"})
public class CityController {

    @Autowired
    private CityService cityService;

    @ApiOperation(value = "Delete City By Id")
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<String>> deleteCity(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                              @PathVariable Long id,
                                                              @RequestHeader("Language") LanguageType lang) {
        cityService.deleteCity(id, lang);
        return lang.equals(LanguageType.ARB) ? Response.success(CITY_REMOVED_SUCCESSFULLY_ARABIC) : Response.success(CITY_REMOVED_SUCCESSFULLY);
    }
}