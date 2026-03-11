package com.apricart.consumer.controller;

import com.apricart.consumer.enity.SalePerson;
import com.apricart.consumer.generic.GenericResponse;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.security.dto.request.SalesPersonRequestDTO;
import com.apricart.consumer.security.dto.response.SalesPersonResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.SalePersonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.SALE_PERSON_MARKED_AS_INACTIVE_SUCCESSFULLY_ARABIC;
import static com.apricart.consumer.security.constants.ResponseMessage.SALE_PERSON_MARKED_AS_INACTIVE_SUCCESSFULLY;

@RestController
@RequestMapping("/v1/auth/open/salesperson")
@Api(value = "Sale Person Controller", tags = {"SalePerson"})
public class SalePersonController {

    @Autowired
    private SalePersonService salePersonService;

    @ApiOperation(value = "Get all salesperson")
    @GetMapping
    public ResponseEntity<GenericResponse<List<SalesPersonResponseDTO>>> getAll(@RequestHeader("Language") LanguageType lang) {
        List<SalePerson> salePersonList = salePersonService.getAll();
        return Response.success(SalePerson.toDTOList(salePersonList));
    }

    @ApiOperation(value = "Get active salesperson")
    @GetMapping("/active")
    public ResponseEntity<GenericResponse<List<SalesPersonResponseDTO>>> getActiveSalesPerson(@RequestHeader("Language") LanguageType lang) {
        List<SalePerson> salePersonList = salePersonService.getActiveSalePersons();
        return Response.success(SalePerson.toDTOList(salePersonList));
    }

    @ApiOperation(value = "Get salesperson by Id")
    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<SalesPersonResponseDTO>> getSalePersonById(@PathVariable("id") Long id, @RequestHeader("Language") LanguageType lang) {
        SalePerson salePerson = salePersonService.findSalePersonById(id);
        return salePerson != null ? Response.success(SalePerson.toDTO(salePerson)) : Response.notFound();
    }

    @ApiOperation(value = "Add Salesperson")
    @PostMapping
    public ResponseEntity<GenericResponse<SalesPersonResponseDTO>> addSalePerson(@Valid @RequestBody SalesPersonRequestDTO salePersonRequest, @RequestHeader("Language") LanguageType lang) {
        salePersonService.addSalePerson(salePersonRequest, lang);
        return Response.created();
    }

    @ApiOperation(value = "Update Salesperson")
    @PutMapping
    public ResponseEntity<GenericResponse<SalesPersonResponseDTO>> updateSalePerson(@RequestBody SalesPersonRequestDTO salePersonRequest, @RequestHeader("Language") LanguageType lang) {
        SalePerson updatedSalePerson = salePersonService.updateSalePerson(salePersonRequest, lang);
        return updatedSalePerson != null ? Response.success(SalePerson.toDTO(updatedSalePerson)) : Response.notFound();
    }

    @ApiOperation(value = "In-active Salesperson by Id")
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<String>> markInactive(@PathVariable("id") Long id, @RequestHeader("Language") LanguageType lang) {
        salePersonService.markInactive(id);
        return lang.equals(LanguageType.ARB) ? Response.success(SALE_PERSON_MARKED_AS_INACTIVE_SUCCESSFULLY_ARABIC) : Response.success(SALE_PERSON_MARKED_AS_INACTIVE_SUCCESSFULLY);
    }

}
