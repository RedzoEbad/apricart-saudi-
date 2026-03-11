package com.apricart.consumer.repository.jpa;

import com.apricart.consumer.enity.FAQ;
import com.apricart.consumer.security.enums.LanguageType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FAQRepository  extends JpaRepository<FAQ, Long> {
    List<FAQ> findBySettingIdAndLanguageType(Long settingId, LanguageType lang);
}
