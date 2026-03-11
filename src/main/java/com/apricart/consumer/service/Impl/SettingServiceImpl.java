package com.apricart.consumer.service.Impl;

import com.apricart.consumer.enity.Setting;
import com.apricart.consumer.exceptions.ResourceNotFoundException;
import com.apricart.consumer.repository.jpa.SettingRepository;
import com.apricart.consumer.security.dto.request.SettingRequestDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.SettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.SETTINGS_NOT_FOUND_ARABIC;
import static com.apricart.consumer.security.constants.ResponseMessage.SETTINGS_NOT_FOUND;

@Service
public class SettingServiceImpl implements SettingService {
    protected static final Logger LOGGER = LoggerFactory.getLogger(SettingServiceImpl.class);

    @Autowired
    private SettingRepository settingRepository;
    private static final String SETTING_ENG = "Setting";
    private static final String SETTING_ARB = "الإعدادات";

    @Override
    public List<Setting> getAllSettings(LanguageType lang) {
        LOGGER.info("Getting all Settings");
        List<Setting> settings = settingRepository.findAll().stream()
                .sorted(Comparator.comparingLong(Setting::getId))
                .collect(Collectors.toList());

        if (!settings.isEmpty()) {
            return settings;
        } else {
            LOGGER.error("No Settings found");
            throw new ResourceNotFoundException(LanguageType.ARB.equals(lang) ? SETTINGS_NOT_FOUND_ARABIC : SETTINGS_NOT_FOUND, true);
        }
    }

    @Override
    public Setting findById(Long id, LanguageType languageType) {
        LOGGER.info("Finding setting by id: {}", id);
        return settingRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Setting with id {} not found", id);
                    return LanguageType.ARB.equals(languageType) ? new ResourceNotFoundException(SETTING_ARB, id, true) : new ResourceNotFoundException(SETTING_ENG, id, false);
                });
    }

    @Override
    public List<Setting> findByWarehouseId(Long warehouseId) {
        LOGGER.info("Finding setting by warehouseId: {}", warehouseId);
        return settingRepository.findByWarehouseId(warehouseId);
    }

    @Override
    public void addSetting(SettingRequestDTO settingRequestDTO) {
        LOGGER.info("Adding Setting: {}", settingRequestDTO);
        save(Setting.fromDTO(settingRequestDTO));
    }

    @Override
    public Setting updateSetting(SettingRequestDTO settingRequestDTO, LanguageType languageType) {
        LOGGER.info("Updating Setting: {}", settingRequestDTO);
        Setting existingSetting = findById(settingRequestDTO.getId(), languageType);

        existingSetting.setWarehouseId(settingRequestDTO.getWarehouseId() == null ? existingSetting.getWarehouseId() : settingRequestDTO.getWarehouseId());
        existingSetting.setAppName(settingRequestDTO.getAppName() == null ? existingSetting.getAppName() : settingRequestDTO.getAppName());
        existingSetting.setAppVersion(settingRequestDTO.getAppVersion() == null ? existingSetting.getAppVersion() : settingRequestDTO.getAppVersion());
        existingSetting.setDeliveryCharges(settingRequestDTO.getDeliveryCharges() == null ? existingSetting.getAppName() : settingRequestDTO.getDeliveryCharges());
        existingSetting.setFacebookURL(settingRequestDTO.getFacebookURL() == null ? existingSetting.getFacebookURL() : settingRequestDTO.getFacebookURL());
        existingSetting.setInstagramURL(settingRequestDTO.getInstagramURL() == null ? existingSetting.getInstagramURL() : settingRequestDTO.getInstagramURL());
        existingSetting.setYoutubeURL(settingRequestDTO.getYoutubeURL() == null ? existingSetting.getYoutubeURL() : settingRequestDTO.getYoutubeURL());
        existingSetting.setKSANumber(settingRequestDTO.getKSANumber() == null ? existingSetting.getKSANumber() : settingRequestDTO.getKSANumber());
        existingSetting.setUKNumber(settingRequestDTO.getUKNumber() == null ? existingSetting.getUKNumber() : settingRequestDTO.getUKNumber());
        existingSetting.setPAKNumber(settingRequestDTO.getPAKNumber() == null ? existingSetting.getPAKNumber() : settingRequestDTO.getPAKNumber());
        existingSetting.setUAENumber(settingRequestDTO.getUAENumber() == null ? existingSetting.getUAENumber() : settingRequestDTO.getUAENumber());
        existingSetting.setPrivacyPolicy(settingRequestDTO.getPrivacyPolicy() == null ? existingSetting.getPrivacyPolicy() : settingRequestDTO.getPrivacyPolicy());
        existingSetting.setTwitterURL(settingRequestDTO.getTwitterURL() == null ? existingSetting.getTwitterURL() : settingRequestDTO.getTwitterURL());
        existingSetting.setMinOrderValue(settingRequestDTO.getMinOrderValue() == null ? existingSetting.getMinOrderValue() : settingRequestDTO.getMinOrderValue());
        existingSetting.setSplashScreen(settingRequestDTO.getSplashScreen() == null ? existingSetting.getSplashScreen() : settingRequestDTO.getSplashScreen());
        existingSetting.setTicker(settingRequestDTO.getTicker() == null ? existingSetting.getTicker() : settingRequestDTO.getTicker());

        return save(existingSetting);
    }

    @Override
    public void deleteSetting(Long id, LanguageType languageType) {
        LOGGER.info("Deactivating setting status for id: {}", id);
        Setting existingSetting = findById(id, languageType);
        if (existingSetting.getStatus()) {
            existingSetting.setStatus(false);
            save(existingSetting);
        }
    }

    @Override
    public Setting save(Setting setting) {
        LOGGER.info("Saving Setting: {}", setting);
        return settingRepository.save(setting);
    }
}
