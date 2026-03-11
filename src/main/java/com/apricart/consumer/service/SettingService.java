package com.apricart.consumer.service;

import com.apricart.consumer.enity.Setting;
import com.apricart.consumer.security.dto.request.SettingRequestDTO;
import com.apricart.consumer.security.enums.LanguageType;

import java.util.List;

public interface SettingService {
    List<Setting> getAllSettings(LanguageType lang);
    Setting findById(Long id, LanguageType languageType);
    List<Setting> findByWarehouseId(Long warehouseId);
    void addSetting(SettingRequestDTO settingRequestDTO);
    Setting updateSetting(SettingRequestDTO settingRequestDTO, LanguageType languageType);
    Setting save(Setting setting);
    void deleteSetting(Long id, LanguageType languageType);
}
