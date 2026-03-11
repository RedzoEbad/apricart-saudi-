package com.apricart.consumer.controller;

import com.apricart.consumer.enity.Warehouse;
import com.apricart.consumer.generic.GenericResponse;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.security.dto.request.WarehouseRequestDTO;
import com.apricart.consumer.security.dto.response.WarehouseResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.WarehouseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.WAREHOUSE_REMOVED_SUCCESSFULLY_ARABIC;
import static com.apricart.consumer.security.constants.ResponseMessage.WAREHOUSE_REMOVED_SUCCESSFULLY;


@RestController
@RequestMapping("/v1/warehouses")
@Api(value = "Warehouse Controller", tags = {"Warehouse"})
public class WarehouseController{

    @Autowired
    private WarehouseService warehouseService;

    @ApiOperation(value = "Get warehouse by Name")
    @GetMapping("/name/{name}")
    public ResponseEntity<GenericResponse<WarehouseResponseDTO>> findWarehouseByName(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                     @PathVariable String name, @RequestHeader("Language") LanguageType lang) {
        Warehouse warehouse = warehouseService.findByName(name);
        return warehouse != null ? Response.success(Warehouse.toDTO(warehouse)) : Response.notFound();
    }

    @ApiOperation(value = "Get warehouse by Id")
    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<WarehouseResponseDTO>> findWarehouseById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                   @PathVariable Long id, @RequestHeader("Language") LanguageType lang) {
        Warehouse warehouse = warehouseService.findById(id, lang);
        return warehouse != null ? Response.success(Warehouse.toDTO(warehouse)) : Response.notFound();
    }

    @ApiOperation(value = "Get warehouse by city Id")
    @GetMapping("/city/{id}")
    public ResponseEntity<GenericResponse<WarehouseResponseDTO>> findWarehouseByCityId(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                             @PathVariable Long id, @RequestHeader("Language") LanguageType lang) {
        WarehouseResponseDTO warehouses = Warehouse.toDTO(warehouseService.findByCityId(id, lang));
        return warehouses != null ? Response.success(warehouses) : Response.notFound();
    }


    @ApiOperation(value = "Add Warehouse")
    @PostMapping
    public ResponseEntity<GenericResponse<WarehouseResponseDTO>> addWarehouse(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                              @Valid @RequestBody WarehouseRequestDTO warehouseRequestDTO, @RequestHeader("Language") LanguageType lang) {
        warehouseService.addWarehouse(warehouseRequestDTO, lang);
        return Response.created();
    }

    @ApiOperation(value = "Update Warehouse")
    @PutMapping
    public ResponseEntity<GenericResponse<WarehouseResponseDTO>> updateWarehouse(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                 @Valid @RequestBody WarehouseRequestDTO warehouseRequestDTO, @RequestHeader("Language") LanguageType lang) {
        Warehouse updatedWarehouse = warehouseService.updateWarehouse(warehouseRequestDTO, lang);
        return updatedWarehouse != null ? Response.success(Warehouse.toDTO(updatedWarehouse)) : Response.notFound();
    }

    @ApiOperation(value = "Update Warehouse Status By Id")
    @PutMapping("/{id}/{status}")
    public ResponseEntity<GenericResponse<WarehouseResponseDTO>> updateWarehouseStatusById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                                           @PathVariable Long id, @PathVariable boolean status, @RequestHeader("Language") LanguageType lang) {
        Warehouse updatedWarehouse = warehouseService.updateWarehouseStatusById(id, status, lang);
        return updatedWarehouse != null ? Response.success(Warehouse.toDTO(updatedWarehouse)) : Response.notFound();
    }

    @ApiOperation(value = "Delete Warehouse By Id")
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<String>> deleteWarehouse(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                   @PathVariable Long id, @RequestHeader("Language") LanguageType lang) {
        warehouseService.deleteWarehouse(id, lang);
        return lang.equals(LanguageType.ARB) ? Response.success(WAREHOUSE_REMOVED_SUCCESSFULLY_ARABIC) : Response.success(WAREHOUSE_REMOVED_SUCCESSFULLY);
    }
}

