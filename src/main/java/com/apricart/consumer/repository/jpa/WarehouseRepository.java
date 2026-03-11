package com.apricart.consumer.repository.jpa;

import com.apricart.consumer.enity.City;
import com.apricart.consumer.enity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created on Feb, 2024
 *
 * @author Kashaf Arshad
 */
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    Warehouse findWarehouseByName(String name);
    Warehouse findWarehouseByCity(City city);
    List<Warehouse> findWarehouseByCityId(Long cityId);
    Warehouse findWarehouseByLatitudeAndLongitudeAndCity(String latitude, String longitude, City city);


}
