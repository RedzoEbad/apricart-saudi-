package com.apricart.consumer.controller;

import com.apricart.consumer.enity.PriceList;
import com.apricart.consumer.generic.GenericResponse;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.security.dto.request.PriceListRequestDTO;
import com.apricart.consumer.security.dto.response.PriceListResponseDTO;
import com.apricart.consumer.security.dto.response.WarehouseResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.security.enums.PriceBookType;
import com.apricart.consumer.service.PriceListService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.PRICE_LIST_REMOVED_SUCCESSFULLY_ARABIC;
import static com.apricart.consumer.security.constants.ResponseMessage.PRICE_LIST_REMOVED_SUCCESSFULLY;

@RestController
@RequestMapping("/v1/pricelists")
@Api(value = "PriceList Controller", tags = {"PriceList"})
public class PriceListController{

    @Autowired
    private PriceListService priceListService;

    @ApiOperation(value = "Get all price lists", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping
    public ResponseEntity<GenericResponse<List<PriceListResponseDTO>>> getAllPriceLists(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                        @RequestHeader("Language") LanguageType lang) {
        List<PriceListResponseDTO> priceLists = PriceList.toDTOList(priceListService.getAllPriceLists());
        return !priceLists.isEmpty() ? Response.success(priceLists) : Response.notFound();
    }

    @ApiOperation(value = "Get price list by Name", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping("/name/{name}")
    public ResponseEntity<GenericResponse<PriceListResponseDTO>> findPriceListByName(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                     @PathVariable String name, @RequestHeader("Language") LanguageType lang) {
        PriceList priceList = priceListService.findByName(name);
        return priceList != null ? Response.success(PriceList.toDTO(priceList)) : Response.notFound();
    }

    @ApiOperation(value = "Get price list by price book", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping("/pricebook/{priceBook}")
    public ResponseEntity<GenericResponse<List<PriceListResponseDTO>>> findPriceListsByPriceBook(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                                 @PathVariable PriceBookType priceBook, @RequestHeader("Language") LanguageType lang) {
        List<PriceListResponseDTO> priceLists = PriceList.toDTOList(priceListService.findByPriceBook(priceBook));
        return !priceLists.isEmpty() ? Response.success(priceLists) : Response.notFound();
    }

    @ApiOperation(value = "Get price list by Id", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<PriceListResponseDTO>> findPriceListById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                   @RequestHeader("Language") LanguageType lang, @PathVariable Long id) {
        PriceList warehouse = priceListService.findById(id, lang);
        return warehouse != null ? Response.success(PriceList.toDTO(warehouse)) : Response.notFound();
    }
    @ApiOperation(value = "Get price list by currency Id", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping("/currency/{id}")
    public ResponseEntity<GenericResponse<List<PriceListResponseDTO>>> findPriceListByCurrencyId(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                                 @RequestHeader("Language") LanguageType lang, @PathVariable Long id) {
        List<PriceListResponseDTO> priceLists = PriceList.toDTOList(priceListService.findByCurrencyId(id, lang));
        return !priceLists.isEmpty() ? Response.success(priceLists) : Response.notFound();
    }

    @ApiOperation(value = "Get all active price lists", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping("/active")
    public ResponseEntity<GenericResponse<List<PriceListResponseDTO>>> getActivePriceLists(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestHeader("Language") LanguageType lang) {
        List<PriceListResponseDTO> priceLists = PriceList.toDTOList(priceListService.getActivePriceLists(lang));
        return !priceLists.isEmpty() ? Response.success(priceLists) : Response.notFound();
    }

    @ApiOperation(value = "Add Price List", authorizations = { @Authorization(value="jwtToken") })
    @PostMapping
    public ResponseEntity<GenericResponse<WarehouseResponseDTO>> addPriceList(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                              @Valid @RequestBody PriceListRequestDTO priceListRequestDTO, @RequestHeader("Language") LanguageType lang) {
        priceListService.addPriceList(priceListRequestDTO, lang);
        return Response.created();
    }

    @ApiOperation(value = "Update Price List", authorizations = { @Authorization(value="jwtToken") })
    @PutMapping
    public ResponseEntity<GenericResponse<PriceListResponseDTO>> updatePriceList(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                 @RequestHeader("Language") LanguageType lang, @Valid @RequestBody PriceListRequestDTO priceListRequestDTO) {
        PriceList updatedPriceList = priceListService.updatePriceList(priceListRequestDTO, lang);
        return updatedPriceList != null ? Response.success(PriceList.toDTO(updatedPriceList)) : Response.notFound();
    }
    @ApiOperation(value = "Update Price List Status By Id", authorizations = { @Authorization(value="jwtToken") })
    @PutMapping("/{id}/{status}")
    public ResponseEntity<GenericResponse<PriceListResponseDTO>> updatePriceListStatusById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                           @RequestHeader("Language") LanguageType lang, @PathVariable Long id, @PathVariable boolean status) {
        PriceList updatedPriceList = priceListService.updatePriceListStatusById(id, status, lang);
        return updatedPriceList != null ? Response.success(PriceList.toDTO(updatedPriceList)) : Response.notFound();
    }

    @ApiOperation(value = "Delete Warehouse By Id", authorizations = { @Authorization(value="jwtToken") })
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<String>> deleteWarehouse(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                   @PathVariable Long id, @RequestHeader("Language") LanguageType lang) {
        priceListService.deletePriceList(id, lang);
        return lang.equals(LanguageType.ARB) ? Response.success(PRICE_LIST_REMOVED_SUCCESSFULLY_ARABIC) : Response.success(PRICE_LIST_REMOVED_SUCCESSFULLY);
    }
}