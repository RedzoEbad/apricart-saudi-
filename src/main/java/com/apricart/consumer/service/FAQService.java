package com.apricart.consumer.service;

import com.apricart.consumer.enity.FAQ;
import com.apricart.consumer.security.dto.request.FAQRequestDTO;
import com.apricart.consumer.security.dto.response.FAQResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;

import java.util.List;

public interface FAQService {
    List<FAQ> getAllFAQs(LanguageType lang);
    FAQ findById(Long id, LanguageType languageType);
    List<FAQResponseDTO> findBySettingIdAndLanguage(Long settingId, LanguageType languageType);
    void addFaq(FAQRequestDTO faqRequestDTO, Long settingId, LanguageType languageType);
    FAQ updateFaq(FAQRequestDTO faqRequestDTO, LanguageType languageType);
    void deleteFaq(Long id, LanguageType languageType);
}
