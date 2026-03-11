package com.apricart.consumer.service;

import com.apricart.consumer.enity.Warehouse;
import com.apricart.consumer.security.dto.request.WarehouseRequestDTO;
import com.apricart.consumer.security.enums.LanguageType;

import java.util.List;

public interface WarehouseService {
    List<Warehouse> getAllWarehouses();
    Warehouse findById(Long id, LanguageType languageType);
    Warehouse findByCityId(Long id, LanguageType languageType);
    List<Warehouse>findWarehousesByCityId(Long id, LanguageType languageType);
    Warehouse findByName(String name);
    Warehouse findByCoordinates(String latitude, String longitude, Long cityId, LanguageType lang);
    List<Warehouse> getActiveWarehouses(LanguageType languageType);
    Warehouse updateWarehouseStatusById(Long id, Boolean status, LanguageType languageType);
    void addWarehouse(WarehouseRequestDTO warehouseRequestDTO, LanguageType languageType);
    Warehouse updateWarehouse(WarehouseRequestDTO warehouseRequestDTO, LanguageType languageType);
    void deleteWarehouse(Long id, LanguageType languageType);
}
