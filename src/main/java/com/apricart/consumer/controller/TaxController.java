package com.apricart.consumer.controller;

import com.apricart.consumer.enity.Tax;
import com.apricart.consumer.generic.GenericResponse;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.security.dto.request.TaxRequestDTO;
import com.apricart.consumer.security.dto.response.TaxResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.TaxService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.TAX_REMOVED_SUCCESSFULLY_ARABIC;
import static com.apricart.consumer.security.constants.ResponseMessage.TAX_REMOVED_SUCCESSFULLY;

@RestController
@RequestMapping("/v1/taxes")
@Api(value = "Tax Controller", tags = {"Tax"})
public class TaxController{

    @Autowired
    private TaxService taxService;

    @ApiOperation(value = "Get all taxes", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping
    public ResponseEntity<GenericResponse<List<TaxResponseDTO>>> getAllTaxes(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestHeader("Language") LanguageType lang) {
        List<TaxResponseDTO> taxes = Tax.toDTOList(taxService.getAllTaxes());
        return !taxes.isEmpty() ? Response.success(taxes) : Response.notFound();
    }

    @ApiOperation(value = "Get Tax by Id", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<TaxResponseDTO>> findTaxById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @PathVariable Long id, @RequestHeader("Language") LanguageType lang) {
        Tax tax = taxService.findById(id, lang);
        return tax != null ? Response.success(Tax.toDTO(tax)) : Response.notFound();
    }

    @ApiOperation(value = "Get Tax by Name", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping("/name/{name}")
    public ResponseEntity<GenericResponse<List<TaxResponseDTO>>> findTaxByName(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @PathVariable String name, @RequestHeader("Language") LanguageType lang) {
        List<TaxResponseDTO> taxes = Tax.toDTOList(taxService.findByName(name));
        return !taxes.isEmpty() ? Response.success(taxes) : Response.notFound();
    }

    @ApiOperation(value = "Get Tax by Type", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping("/type/{type}")
    public ResponseEntity<GenericResponse<List<TaxResponseDTO>>> findTaxByType(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @PathVariable String type, @RequestHeader("Language") LanguageType lang) {
        List<TaxResponseDTO> taxes = Tax.toDTOList(taxService.findByType(type));
        return !taxes.isEmpty() ? Response.success(taxes) : Response.notFound();
    }

    @ApiOperation(value = "Get Tax by Country Code", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping("/country/{country}")
    public ResponseEntity<GenericResponse<List<TaxResponseDTO>>> findTaxByCountryCode(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @PathVariable String country, @RequestHeader("Language") LanguageType lang) {
        List<TaxResponseDTO> taxes = Tax.toDTOList(taxService.findByCountryCode(country));
        return !taxes.isEmpty() ? Response.success(taxes) : Response.notFound();
    }

    @ApiOperation(value = "Get Tax by Authority", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping("/authority/{authority}")
    public ResponseEntity<GenericResponse<TaxResponseDTO>> findTaxByAuthority(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @PathVariable String authority, @RequestHeader("Language") LanguageType lang) {
        Tax tax = taxService.findByAuthority(authority);
        return tax != null ? Response.success(Tax.toDTO(tax)) : Response.notFound();
    }

    @ApiOperation(value = "Get Tax by Authority", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping("/{percentage}/{amount}")
    public ResponseEntity<GenericResponse<String>> getTaxAmount(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @PathVariable String percentage, @PathVariable String amount, @RequestHeader("Language") LanguageType lang) {
        String taxAmount = taxService.getTaxAmount(percentage, amount, lang).toString();
        return taxAmount != null ? Response.success(taxAmount) : Response.notFound();
    }

    @ApiOperation(value = "Add Tax", authorizations = { @Authorization(value="jwtToken") })
    @PostMapping
    public ResponseEntity<GenericResponse<TaxResponseDTO>> addTax(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @Valid @RequestBody TaxRequestDTO taxRequestDTO, @RequestHeader("Language") LanguageType lang) {
        taxService.addTax(taxRequestDTO);
        return Response.created();
    }

    @ApiOperation(value = "Update Tax", authorizations = { @Authorization(value="jwtToken") })
    @PutMapping
    public ResponseEntity<GenericResponse<TaxResponseDTO>> updateTax(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @Valid @RequestBody TaxRequestDTO taxRequestDTO, @RequestHeader("Language") LanguageType lang) {
        Tax updatedTax = taxService.updateTax(taxRequestDTO, lang);
        return updatedTax != null ? Response.success(Tax.toDTO(updatedTax)) : Response.notFound();
    }

    @ApiOperation(value = "Delete Tax By Id", authorizations = { @Authorization(value="jwtToken") })
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<String>> deleteTax(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @PathVariable Long id, @RequestHeader("Language") LanguageType lang) {
        taxService.deleteTax(id, lang);
        return lang.equals(LanguageType.ARB) ? Response.success(TAX_REMOVED_SUCCESSFULLY_ARABIC) : Response.success(TAX_REMOVED_SUCCESSFULLY);
    }

}