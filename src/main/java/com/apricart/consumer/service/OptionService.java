package com.apricart.consumer.service;

import com.apricart.consumer.enity.Option;
import com.apricart.consumer.security.dto.request.OptionRequestDTO;
import com.apricart.consumer.security.enums.LanguageType;

import java.util.List;

public interface OptionService {
    List<Option> getAllOptions();
    Option findById(Long id, LanguageType languageType);
    Option findByKey(String key);
    List<Option> findByKeys(List<String> key);
    String findValueByKey(String key, LanguageType lang);
    Option updateOptionStatusById(Long id, Boolean status, LanguageType languageType);
    List<Option> getActiveOptions(LanguageType lang);
    void addOption(OptionRequestDTO optionRequestDTO);
    Option updateOption(OptionRequestDTO optionRequestDTO, LanguageType languageType);
    void disableOption(Long id, LanguageType languageType);
}
