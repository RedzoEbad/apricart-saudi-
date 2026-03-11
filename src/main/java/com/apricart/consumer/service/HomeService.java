package com.apricart.consumer.service;

import com.apricart.consumer.security.dto.response.HomeDTO;
import com.apricart.consumer.security.enums.LanguageType;
public interface HomeService {

    HomeDTO getHomeDetailsByWarehouseId(Long warehouseId, LanguageType lang, Long customerId, int pageNo, int pageSize, LanguageType languageType);
}
