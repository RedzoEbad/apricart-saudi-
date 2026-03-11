package com.apricart.consumer.service.Impl;

import com.apricart.consumer.enity.Option;
import com.apricart.consumer.exceptions.ResourceNotFoundException;
import com.apricart.consumer.repository.jpa.OptionRepository;
import com.apricart.consumer.security.dto.request.OptionRequestDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.OptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.OPTION_STATUS_NOT_ACTIVE_ARABIC;
import static com.apricart.consumer.security.constants.ArabicResponseMessages.OPTION_VALUE_NOT_FOUND_ARABIC;
import static com.apricart.consumer.security.constants.ResponseMessage.OPTION_STATUS_NOT_ACTIVE;
import static com.apricart.consumer.security.constants.ResponseMessage.OPTION_VALUE_NOT_FOUND;

@Service
public class OptionServiceImpl implements OptionService {
    protected static final Logger LOGGER = LoggerFactory.getLogger(OptionServiceImpl.class);

    @Autowired
    private OptionRepository optionRepository;
    private static final String OPTION_ENG = "Option";
    private static final String OPTION_ARB = "الخيار";

    @Override
    public List<Option> getAllOptions() {
        LOGGER.info("Getting all options");
        return optionRepository.findAll();
    }

    @Override
    public Option findById(Long id, LanguageType languageType) {
        LOGGER.info("Finding option by id: {}", id);
        return optionRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Option with id {} not found", id);
                    return LanguageType.ARB.equals(languageType) ?  new ResourceNotFoundException(OPTION_ARB, id, true) : new ResourceNotFoundException(OPTION_ENG, id, false);
                });
    }

    @Override
    public Option findByKey(String key) {
        return optionRepository.findByKey(key);
    }

    @Override
    public List<Option> findByKeys(List<String> keys) {
        List<Option> options = new ArrayList<>();
        for (String key : keys) {
            Option option = findByKey(key);
            if (option != null) {
                options.add(option);
            }
        }
        return options;
    }

    @Override
    public String findValueByKey(String key, LanguageType lang) {
        LOGGER.info("Finding option value by key: {}", key);
        Option option = optionRepository.findByKey(key);
        if (option != null) {
            return option.getValue();
        } else {
            LOGGER.error("Option value with key {} not found", key);
            throw new ResourceNotFoundException(LanguageType.ARB.equals(lang) ? key+OPTION_VALUE_NOT_FOUND_ARABIC+key : OPTION_VALUE_NOT_FOUND, true);
        }
    }

    @Override
    public List<Option> getActiveOptions(LanguageType lang) {
        LOGGER.info("Getting active options");
        List<Option> options = optionRepository.findAll().stream()
                .filter(Option::getStatus)
                .sorted(Comparator.comparingLong(Option::getId))
                .collect(Collectors.toList());

        if (!options.isEmpty()) {
            return options;
        } else {
            LOGGER.error("No active option found");
            throw new ResourceNotFoundException(LanguageType.ARB.equals(lang) ? OPTION_STATUS_NOT_ACTIVE_ARABIC : OPTION_STATUS_NOT_ACTIVE, true);
        }
    }

    @Override
    public void addOption(OptionRequestDTO optionRequestDTO) {
        LOGGER.info("Adding option: {}", optionRequestDTO);
        save(Option.fromDTO(optionRequestDTO));
    }

    @Override
    public Option updateOption(OptionRequestDTO optionRequestDTO, LanguageType languageType) {
        LOGGER.info("Updating option: {}", optionRequestDTO);
        Option existingOption = findById(optionRequestDTO.getId(), languageType);

        existingOption.setKey(optionRequestDTO.getKey() == null ? existingOption.getKey() : optionRequestDTO.getKey());
        existingOption.setValue(optionRequestDTO.getValue() == null ? existingOption.getValue() : optionRequestDTO.getValue());
        existingOption.setStatus(optionRequestDTO.getStatus() == null ? existingOption.getStatus() : optionRequestDTO.getStatus());
        return save(existingOption);
    }

    @Override
    public void disableOption(Long id, LanguageType languageType) {
        LOGGER.info("Disabling option for id: {}", id);
        Option existingOption = findById(id,languageType);
        if(existingOption.getStatus()) {
            existingOption.setStatus(false);
            save(existingOption);
        }
    }
    public Option save(Option option) {
        LOGGER.info("Saving option: {}", option);
        return optionRepository.save(option);
    }
    @Override
    public Option updateOptionStatusById(Long id, Boolean status, LanguageType languageType) {
        LOGGER.info("Updating option status by id: {} to {}", id, status);
        Option existingOption = findById(id, languageType);
        existingOption.setStatus(status == null ? existingOption.getStatus() : status);
        return save(existingOption);
    }
}
