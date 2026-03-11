package com.apricart.consumer.service;

import com.apricart.consumer.enity.Product;
import com.apricart.consumer.enity.ProductWarehouse;

import java.util.List;

public interface SearchService {

    List<ProductWarehouse> searchProduct(String query, Long warehouseId);

}


