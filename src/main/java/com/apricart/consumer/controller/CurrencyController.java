package com.apricart.consumer.controller;

import com.apricart.consumer.enity.Currency;
import com.apricart.consumer.generic.GenericResponse;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.security.constants.ArabicResponseMessages;
import com.apricart.consumer.security.constants.ResponseMessage;
import com.apricart.consumer.security.dto.request.CurrencyRequestDTO;
import com.apricart.consumer.security.dto.response.CurrencyResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.CurrencyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/currencies")
@Api(value = "Currency Controller", tags = {"Currency"})
public class CurrencyController{

    @Autowired
    private CurrencyService currencyService;

    @ApiOperation(value = "Get all currencies")
    @GetMapping
    public ResponseEntity<GenericResponse<List<CurrencyResponseDTO>>> getAllCurrencies(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                       @RequestHeader("Language") LanguageType lang) {
        List<CurrencyResponseDTO> currencyList = Currency.toDTOList(currencyService.getAllCurrencies());
        return !currencyList.isEmpty() ? Response.success(currencyList) : Response.notFound();
    }

    @ApiOperation(value = "Get all active currencies")
    @GetMapping("/active")
    public ResponseEntity<GenericResponse<List<CurrencyResponseDTO>>> getActiveCurrencies(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                          @RequestHeader("Language") LanguageType lang) {
        List<CurrencyResponseDTO> currencyList = Currency.toDTOList(currencyService.getActiveCurrencies(lang));
        return !currencyList.isEmpty() ? Response.success(currencyList) : Response.notFound();
    }

    @ApiOperation(value = "Get currency by Id")
    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<CurrencyResponseDTO>> findCurrencyById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                 @PathVariable Long id, @RequestHeader("Language") LanguageType lang) {
        Currency currency = currencyService.findById(id, lang);
        return currency != null ? Response.success(Currency.toDTO(currency)) : Response.notFound();
    }

    @ApiOperation(value = "Get currency by code")
    @GetMapping("/code/{code}")
    public ResponseEntity<GenericResponse<CurrencyResponseDTO>> findCurrencyByCode(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                   @PathVariable String code, @RequestHeader("Language") LanguageType lang) {
        Currency currency = currencyService.findByCurrencyCode(code);
        return currency != null ? Response.success(Currency.toDTO(currency)) : Response.notFound();
    }

    @ApiOperation(value = "Get currency by symbol")
    @GetMapping("/symbol/{symbol}")
    public ResponseEntity<GenericResponse<List<CurrencyResponseDTO>>> findCurrencyBySymbol(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                           @PathVariable String symbol, @RequestHeader("Language") LanguageType lang) {
        List<CurrencyResponseDTO> currencyList = Currency.toDTOList(currencyService.findByCurrencySymbol(symbol));
        return !currencyList.isEmpty()  ? Response.success(currencyList) : Response.notFound();
    }

    @ApiOperation(value = "Get currency format by Id")
    @GetMapping("/format/{id}")
    public ResponseEntity<GenericResponse<String>> getFormatById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                 @PathVariable Long id, @RequestHeader("Language") LanguageType lang) {
        String currencyFormat = currencyService.getCurrencyFormat(id, lang);
        return currencyFormat != null  ? Response.success(currencyFormat) : Response.notFound();
    }

    @ApiOperation(value = "Get currency precision by Id")
    @GetMapping("/precision/{id}")
    public ResponseEntity<GenericResponse<String>> getPrecisionById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                    @PathVariable Long id, @RequestHeader("Language") LanguageType lang) {
        String currencyPrecision = currencyService.getCurrencyPrecision(id, lang);
        return currencyPrecision != null  ? Response.success(currencyPrecision) : Response.notFound();
    }

    @ApiOperation(value = "Get currency precision by value")
    @GetMapping("/format/{value}/{id}")
    public ResponseEntity<GenericResponse<String>> formatCurrencyByValue(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                         @PathVariable String value, @PathVariable Long id, @RequestHeader("Language") LanguageType lang) {
        String currencyValue = currencyService.formatCurrency(value,id, lang);
        return currencyValue != null  ? Response.success(currencyValue) : Response.notFound();
    }

    @ApiOperation(value = "Add Currency")
    @PostMapping
    public ResponseEntity<GenericResponse<CurrencyResponseDTO>> addCurrency(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                            @Valid @RequestBody CurrencyRequestDTO currencyRequestDTO, @RequestHeader("Language") LanguageType lang) {
        currencyService.addCurrency(currencyRequestDTO);
        return Response.created();
    }

    @ApiOperation(value = "Update Currency")
    @PutMapping
    public ResponseEntity<GenericResponse<CurrencyResponseDTO>> updateCurrency(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                               @RequestBody  CurrencyRequestDTO currencyRequestDTO, @RequestHeader("Language") LanguageType lang) {
        Currency updatedCurrency = currencyService.updateCurrency(currencyRequestDTO, lang);
        return updatedCurrency != null ? Response.success(Currency.toDTO(updatedCurrency)) : Response.notFound();
    }

    @ApiOperation(value = "Delete Currency By Id")
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<String>> deleteCurrency(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                  @PathVariable Long id, @RequestHeader("Language") LanguageType lang) {
        currencyService.deleteCurrency(id, lang);
        return lang.equals(LanguageType.ARB) ? Response.success(ArabicResponseMessages.CURRENCY_REMOVED_SUCCESSFULLY_ARABIC) : Response.success(ResponseMessage.CURRENCY_REMOVED_SUCCESSFULLY);
    }
}
