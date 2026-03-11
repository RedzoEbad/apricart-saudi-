package com.apricart.consumer.service.Impl;

import com.apricart.consumer.enity.FAQ;
import com.apricart.consumer.enity.Setting;
import com.apricart.consumer.exceptions.ResourceNotFoundException;
import com.apricart.consumer.repository.jpa.FAQRepository;
import com.apricart.consumer.security.dto.request.FAQRequestDTO;
import com.apricart.consumer.security.dto.response.FAQResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.FAQService;
import com.apricart.consumer.service.SettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.FAQ_NOT_FOUND_ARABIC;
import static com.apricart.consumer.security.constants.ResponseMessage.FAQ_NOT_FOUND;

@Service
public class FAQServiceImpl implements FAQService {
    protected static final Logger LOGGER = LoggerFactory.getLogger(BrandServiceImpl.class);
    private static final String FAQ_ENG = "FAQ";
    private static final String FAQ_ARB = "الأسئلة الشائعة";
    @Autowired
    private FAQRepository faqRepository;
    @Autowired
    private SettingService settingService;

    @Override
    public List<FAQ> getAllFAQs(LanguageType lang) {
        LOGGER.info("Getting all FAQs");
        List<FAQ> faqs = faqRepository.findAll().stream()
                .filter(faq -> lang.equals(faq.getLanguageType()))
                .sorted(Comparator.comparingLong(FAQ::getId))
                .collect(Collectors.toList());

        if (!faqs.isEmpty()) {
            return faqs;
        } else {
            LOGGER.error("No FAQ found");
            throw new ResourceNotFoundException(LanguageType.ARB.equals(lang) ? FAQ_NOT_FOUND_ARABIC : FAQ_NOT_FOUND, true);
        }
    }

    @Override
    public FAQ findById(Long id, LanguageType lang) {
        LOGGER.info("Finding FAQ by id: {}", id);
        return faqRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("FAQ with id {} not found", id);
                    return LanguageType.ARB.equals(lang) ? new ResourceNotFoundException(FAQ_ARB, id, true) : new ResourceNotFoundException(FAQ_ENG, id, false);
                });
    }

    @Override
    public List<FAQResponseDTO> findBySettingIdAndLanguage(Long settingId, LanguageType languageType) {

        LOGGER.info("Finding FAQ by settingId: {}", settingId);
        return FAQ.toDTOList(faqRepository.findBySettingIdAndLanguageType(settingId, languageType));
    }

    @Override
    public void addFaq(FAQRequestDTO faqRequestDTO, Long settingId, LanguageType languageType) {
        LOGGER.info("Finding Setting: {}", settingId);
        Setting setting = settingService.findById(settingId, languageType);

        LOGGER.info("Adding FAQ to Setting: {}", faqRequestDTO);
        FAQ faq = FAQ.fromDTO(faqRequestDTO);
        faq.setSetting(setting);
        faq.setLanguageType(languageType);
        save(faq);
    }

    @Override
    public FAQ updateFaq(FAQRequestDTO faqRequestDTO, LanguageType languageType) {
        LOGGER.info("Updating FAQ: {}", faqRequestDTO);
        FAQ existingFAQ = findById(faqRequestDTO.getId(), languageType);

        existingFAQ.setQuestion(faqRequestDTO.getQuestion() == null ? existingFAQ.getQuestion() : faqRequestDTO.getQuestion());
        existingFAQ.setAnswer(faqRequestDTO.getAnswer() == null ? existingFAQ.getAnswer() : faqRequestDTO.getAnswer());
        existingFAQ.setStatus(faqRequestDTO.getStatus() == null ? existingFAQ.getStatus() : faqRequestDTO.getStatus());

        return save(existingFAQ);
    }

    @Override
    public void deleteFaq(Long id, LanguageType languageType) {
        LOGGER.info("Deleting FAQ for id: {}", id);
        if (!faqRepository.existsById(id)) {
            LOGGER.error("FAQ with id {} not found for removing", id);
            throw LanguageType.ARB.equals(languageType) ? new ResourceNotFoundException(FAQ_ARB, id, true) : new ResourceNotFoundException(FAQ_ENG, id, false);
        }
        faqRepository.deleteById(id);
    }

    public FAQ save(FAQ faq) {
        LOGGER.info("Saving FAQ: {}", faq);
        return faqRepository.save(faq);
    }
}
