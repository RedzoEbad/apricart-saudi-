package com.apricart.consumer.service.Impl;

import com.apricart.consumer.enity.DeliveryTime;
import com.apricart.consumer.enity.Setting;
import com.apricart.consumer.exceptions.ResourceNotFoundException;
import com.apricart.consumer.repository.jpa.DeliveryTimeRepository;
import com.apricart.consumer.security.dto.request.DeliveryTimeRequestDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.DeliveryTimeService;
import com.apricart.consumer.service.SettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.DELIVERY_TIME_NOT_FOUND_ARABIC;
import static com.apricart.consumer.security.constants.ResponseMessage.DELIVERY_TIME_NOT_FOUND;

@Service
public class DeliveryTimeServiceImpl implements DeliveryTimeService {
    protected static final Logger LOGGER = LoggerFactory.getLogger(DeliveryTimeServiceImpl.class);
    private static final String DELIVERY_TIME_ENG = "Delivery Time";
    private static final String DELIVERY_TIME_ARB = "وقت التسليم";
    @Autowired
    private DeliveryTimeRepository deliveryTimeRepository;
    @Autowired
    private SettingService settingService;

    @Override
    public List<DeliveryTime> getAllDeliveryTimes(LanguageType lang) {
        LOGGER.info("Getting all DeliveryTimes");
        List<DeliveryTime> deliveryTimes = deliveryTimeRepository.findAll().stream()
                .sorted(Comparator.comparingLong(DeliveryTime::getId))
                .collect(Collectors.toList());

        if (!deliveryTimes.isEmpty()) {
            return deliveryTimes;
        } else {
            LOGGER.error("No DeliveryTimes found");
            throw new ResourceNotFoundException(LanguageType.ARB.equals(lang) ? DELIVERY_TIME_NOT_FOUND_ARABIC : DELIVERY_TIME_NOT_FOUND, false);
        }
    }

    @Override
    public DeliveryTime findById(Long id, LanguageType languageType) {
        LOGGER.info("Finding DeliveryTime by id: {}", id);
        return deliveryTimeRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("DeliveryTime with id {} not found", id);
                    return LanguageType.ARB.equals(languageType) ? new ResourceNotFoundException(DELIVERY_TIME_ARB, id, true) : new ResourceNotFoundException(DELIVERY_TIME_ENG, id, false);
                });
    }

    @Override
    public List<DeliveryTime> findBySettingId(Long settingId) {
        LOGGER.info("Finding DeliveryTime by settingId: {}", settingId);
        return deliveryTimeRepository.findBySettingId(settingId);
    }

    @Override
    public void addDeliveryTime(DeliveryTimeRequestDTO deliveryTimeRequestDTO, Long settingId, LanguageType languageType) {
        LOGGER.info("Finding Setting: {}", settingId);
        Setting setting = settingService.findById(settingId, languageType);

        LOGGER.info("Adding DeliveryTime: {}", deliveryTimeRequestDTO);
        DeliveryTime deliveryTime = DeliveryTime.fromDTO(deliveryTimeRequestDTO);

        deliveryTime.setSetting(setting);
        save(deliveryTime);
    }

    @Override
    public DeliveryTime updateDeliveryTime(DeliveryTimeRequestDTO deliveryTimeRequestDTO, LanguageType languageType) {
        LOGGER.info("Updating DeliveryTime: {}", deliveryTimeRequestDTO);
        DeliveryTime existingDeliveryTime = findById(deliveryTimeRequestDTO.getId(), languageType);

        existingDeliveryTime.setDeliveryTime(deliveryTimeRequestDTO.getDeliveryTime() == null ? existingDeliveryTime.getDeliveryTime() : deliveryTimeRequestDTO.getDeliveryTime());
        existingDeliveryTime.setStatus(deliveryTimeRequestDTO.getStatus() == null ? existingDeliveryTime.getStatus() : deliveryTimeRequestDTO.getStatus());

        return save(existingDeliveryTime);
    }

    @Override
    public void deleteDeliveryTime(Long id, LanguageType languageType) {
        LOGGER.info("Deleting DeliveryTime for id: {}", id);
        if (!deliveryTimeRepository.existsById(id)) {
            LOGGER.error("DeliveryTime with id {} not found for removing", id);
            throw LanguageType.ARB.equals(languageType) ? new ResourceNotFoundException(DELIVERY_TIME_ARB, id, true) : new ResourceNotFoundException(DELIVERY_TIME_ENG, id, false);
        }
        deliveryTimeRepository.deleteById(id);
    }

    public DeliveryTime save(DeliveryTime deliveryTime) {
        LOGGER.info("Saving DeliveryTime: {}", deliveryTime);
        return deliveryTimeRepository.save(deliveryTime);
    }
}
