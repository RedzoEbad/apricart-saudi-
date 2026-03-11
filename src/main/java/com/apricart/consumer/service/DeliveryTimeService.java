package com.apricart.consumer.service;

import com.apricart.consumer.enity.DeliveryTime;
import com.apricart.consumer.security.dto.request.DeliveryTimeRequestDTO;
import com.apricart.consumer.security.enums.LanguageType;

import java.util.List;

public interface DeliveryTimeService {
    List<DeliveryTime> getAllDeliveryTimes(LanguageType lang);
    DeliveryTime findById(Long id, LanguageType languageType);
    List<DeliveryTime> findBySettingId(Long settingId);
    void addDeliveryTime(DeliveryTimeRequestDTO deliveryTimeRequestDTO, Long settingId, LanguageType languageType);
    DeliveryTime updateDeliveryTime(DeliveryTimeRequestDTO deliveryTimeRequestDTO, LanguageType languageType);
    void deleteDeliveryTime(Long id, LanguageType languageType);
}
