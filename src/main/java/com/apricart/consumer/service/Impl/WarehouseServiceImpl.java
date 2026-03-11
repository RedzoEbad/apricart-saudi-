package com.apricart.consumer.service.Impl;

import com.apricart.consumer.enity.City;
import com.apricart.consumer.enity.Warehouse;
import com.apricart.consumer.exceptions.ResourceNotFoundException;
import com.apricart.consumer.repository.jpa.WarehouseRepository;
import com.apricart.consumer.security.dto.request.WarehouseRequestDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.CityService;
import com.apricart.consumer.service.WarehouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.*;
import static com.apricart.consumer.security.constants.ResponseMessage.*;

@Service
public class WarehouseServiceImpl implements WarehouseService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseServiceImpl.class);
    private static final double DISTANCE_THRESHOLD = 100;
    private static final String WAREHOUSE_ENG = "Warehouse";
    private static final String WAREHOUSE_ARB = "المستودع";

    @Autowired
    WarehouseRepository warehouseRepository;

    @Autowired
    CityService cityService;

    @Override
    public List<Warehouse> getAllWarehouses() {
        LOGGER.info("Retrieving all warehouses");
        return warehouseRepository.findAll();
    }

    @Override
    public Warehouse findById(Long id, LanguageType languageType) {
        LOGGER.info("Finding warehouse by id: {}", id);
        return warehouseRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Warehouse with id {} not found", id);
                    return LanguageType.ARB.equals(languageType) ? new ResourceNotFoundException(WAREHOUSE_ARB, id, true) : new ResourceNotFoundException(WAREHOUSE_ENG, id, false);
                });
    }

    @Override
    public Warehouse findByCityId(Long id, LanguageType languageType) {
        LOGGER.info("Finding warehouses by city id: {}", id);
        City city = cityService.findById(id, languageType);
        return warehouseRepository.findWarehouseByCity(city);
    }

    @Override
    public List<Warehouse> findWarehousesByCityId(Long id, LanguageType languageType) {
        LOGGER.info("Finding List warehouses by city id: {}", id);

        City city = cityService.findById(id, languageType);
        return warehouseRepository.findWarehouseByCityId(city.getId());
    }

    @Override
    public Warehouse findByName(String name) {
        LOGGER.info("Finding warehouse by name: {}", name);
        return warehouseRepository.findWarehouseByName(name);
    }

    @Override
    public Warehouse findByCoordinates(String latitude, String longitude, Long cityId, LanguageType lang) {
        LOGGER.info("Finding warehouse by coordinates - Latitude: {}, Longitude: {}", latitude, longitude);
        double userLatitude = Double.parseDouble(latitude);
        double userLongitude = Double.parseDouble(longitude);
        City city = cityService.findById(cityId, lang);

        if (!isWithinCityRange(userLatitude, userLongitude, city, DISTANCE_THRESHOLD)) {
            String errorMessage = lang.equals(LanguageType.ARB)
                    ? DELIVERY_NOT_AVAILABLE_MESSAGE_ARABIC
                    : DELIVERY_NOT_AVAILABLE_MESSAGE;
            throw new ResourceNotFoundException(errorMessage, true);
        }

        Warehouse warehouse = findClosestWarehouse(userLatitude, userLongitude, city);
        if (warehouse != null) {
            return warehouse;
        } else {
            String errorMessage = lang.equals(LanguageType.ARB)
                    ? String.format(WAREHOUSE_NOT_FOUND_CITY_ARABIC, city.getArabicName())
                    : String.format(WAREHOUSE_NOT_FOUND_CITY, city.getName());
            throw new ResourceNotFoundException(errorMessage, true);
        }
    }

    @Override
    public List<Warehouse> getActiveWarehouses(LanguageType languageType) {
        LOGGER.info("Retrieving active warehouses");
        List<Warehouse> warehouses = warehouseRepository.findAll().stream()
                .filter(Warehouse::isActive)
                .collect(Collectors.toList());

        if (!warehouses.isEmpty()) {
            return warehouses;
        } else {
            LOGGER.error("No active warehouse found");
            throw LanguageType.ARB.equals(languageType) ? new ResourceNotFoundException(WAREHOUSE_NOT_ACTIVE_ARABIC, true) : new ResourceNotFoundException(WAREHOUSE_NOT_ACTIVE, true);
        }
    }

    @Override
    public Warehouse updateWarehouseStatusById(Long id, Boolean status, LanguageType languageType) {
        LOGGER.info("Updating warehouse status by id: {} to {}", id, status);
        Warehouse existingWarehouse = findById(id, languageType);
        existingWarehouse.setActive(status == null ? existingWarehouse.isActive() : status);
        return save(existingWarehouse);
    }

    @Override
    public void addWarehouse(WarehouseRequestDTO warehouseRequestDTO, LanguageType languageType) {
        LOGGER.info("Adding warehouse: {}", warehouseRequestDTO);
        Warehouse warehouse;
        if (warehouseRequestDTO.getId() != null) {
            warehouse = findById(warehouseRequestDTO.getId(), languageType);
        }
        warehouse = Warehouse.fromDTO(warehouseRequestDTO);
        warehouse.setCity(cityService.findById(warehouseRequestDTO.getCityId(), languageType));
        save(warehouse);
    }

    @Override
    public Warehouse updateWarehouse(WarehouseRequestDTO warehouseRequestDTO, LanguageType languageType) {
        LOGGER.info("Updating warehouse: {}", warehouseRequestDTO);
        Warehouse existingWarehouse = findById(warehouseRequestDTO.getId(), languageType);

        existingWarehouse.setName(warehouseRequestDTO.getName() == null ? existingWarehouse.getName() : warehouseRequestDTO.getName());
        existingWarehouse.setAddress(warehouseRequestDTO.getAddress() == null ? existingWarehouse.getAddress() : warehouseRequestDTO.getAddress());
        existingWarehouse.setLatitude(warehouseRequestDTO.getLatitude() == null ? existingWarehouse.getLatitude() : warehouseRequestDTO.getLatitude());
        existingWarehouse.setLongitude(warehouseRequestDTO.getLongitude() == null ? existingWarehouse.getLongitude() : warehouseRequestDTO.getLongitude());
        existingWarehouse.setCity(warehouseRequestDTO.getCityId() == null ? existingWarehouse.getCity() : cityService.findById(warehouseRequestDTO.getCityId(), languageType));
        return save(existingWarehouse);
    }

    @Override
    public void deleteWarehouse(Long id, LanguageType languageType) {
        LOGGER.info("Deactivating warehouse status for id: {}", id);
        Warehouse existingWarehouse = findById(id, languageType);
        if (existingWarehouse.isActive()) {
            existingWarehouse.setActive(false);
            save(existingWarehouse);
        }
    }

    public Warehouse save(Warehouse warehouse) {
        LOGGER.info("Saving warehouse: {}", warehouse);
        return warehouseRepository.save(warehouse);
    }

    public Warehouse findClosestWarehouse(double userLatitude, double userLongitude, City city) {
        List<Warehouse> filteredWarehouses = warehouseRepository.findAll().stream()
                .filter(warehouse -> warehouse.getCity() == city)
                .collect(Collectors.toList());

        Warehouse closestWarehouse = null;
        double smallestDistance = Double.MAX_VALUE;

        for (Warehouse warehouse : filteredWarehouses) {
            double distance = calculateDistance(userLatitude, userLongitude, Double.parseDouble(warehouse.getLatitude()), Double.parseDouble(warehouse.getLongitude()));
            if (distance < smallestDistance) {
                smallestDistance = distance;
                closestWarehouse = warehouse;
            }
        }
        return smallestDistance <= DISTANCE_THRESHOLD ? closestWarehouse : null;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    public boolean isWithinCityRange(Double userLatitude, Double userLongitude, City city, Double range) {
        List<Warehouse> warehousesInCity = warehouseRepository.findAll().stream()
                .filter(warehouse -> warehouse.getCity().equals(city))
                .collect(Collectors.toList());

        for (Warehouse warehouse : warehousesInCity) {
            double warehouseLatitude = Double.parseDouble(warehouse.getLatitude());
            double warehouseLongitude = Double.parseDouble(warehouse.getLongitude());
            double distance = calculateDistance(userLatitude, userLongitude, warehouseLatitude, warehouseLongitude);
            if (distance <= range) {
                return true;
            }
        }

        return warehouseRepository.findAll().stream()
                .anyMatch(warehouse -> {
                    double warehouseLatitude = Double.parseDouble(warehouse.getLatitude());
                    double warehouseLongitude = Double.parseDouble(warehouse.getLongitude());
                    return areCoordinatesSame(userLatitude, userLongitude, warehouseLatitude, warehouseLongitude)
                            && !warehouse.getCity().equals(city);
                });
    }

    private boolean areCoordinatesSame(Double lat1, Double lon1, Double lat2, Double lon2) {
        return lat1.intValue() == lat2.intValue() && lon1.intValue() == lon2.intValue();
    }
}