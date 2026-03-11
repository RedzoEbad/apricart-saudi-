package com.apricart.consumer.controller;

import com.apricart.consumer.enity.Warehouse;
import com.apricart.consumer.generic.GenericResponse;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.security.dto.response.WarehouseResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.CityService;
import com.apricart.consumer.service.WarehouseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/v1/auth/open/warehouses")
@Api(value = "Warehouse Controller", tags = {"Warehouse"})
public class WarehouseOpenController {

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private CityService cityService;

    @ApiOperation(value = "Get all warehouses" )
    @GetMapping
    public ResponseEntity<GenericResponse<List<WarehouseResponseDTO>>> getAllWarehouses(@RequestHeader("Language") LanguageType lang) {
        List<WarehouseResponseDTO> warehouses = Warehouse.toDTOList(warehouseService.getAllWarehouses());
        return !warehouses.isEmpty() ? Response.success(warehouses) : Response.notFound();
    }

    @ApiOperation(value = "Get all active warehouses")
    @GetMapping("/active")
    public ResponseEntity<GenericResponse<List<WarehouseResponseDTO>>> getActiveWarehouses(@RequestHeader("Language") LanguageType lang) {
        List<WarehouseResponseDTO> warehouses = Warehouse.toDTOList(warehouseService.getActiveWarehouses(lang));
        return !warehouses.isEmpty() ? Response.success(warehouses) : Response.notFound();
    }

    @ApiOperation(value = "Get warehouse by coordinates")
    @GetMapping("/coordinates/{lat}/{lon}")
    public ResponseEntity<GenericResponse<WarehouseResponseDTO>> findWarehouseByCoordinates(@PathVariable String lat, @PathVariable String lon,
                                                                                            @RequestHeader("Language") LanguageType lang,
                                                                                            @RequestParam("cityId") Long cityId) {
        WarehouseResponseDTO warehouse = Warehouse.toDTO(warehouseService.findByCoordinates(lat, lon, cityId, lang));
        return Response.success(warehouse);
    }

}