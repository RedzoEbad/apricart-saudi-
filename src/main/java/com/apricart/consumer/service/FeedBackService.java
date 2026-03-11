package com.apricart.consumer.service;

import com.apricart.consumer.enity.FeedBack;
import com.apricart.consumer.security.dto.request.FeedBackRequestDTO;
import com.apricart.consumer.security.enums.StatusType;
import com.apricart.consumer.security.enums.LanguageType;

import java.util.List;

public interface FeedBackService {
    void addFeedBack(FeedBackRequestDTO dto, LanguageType lang);
    FeedBack updateFeedBackStatus(StatusType statusType, Long feedBackId, LanguageType lang);
    List<FeedBack> findAllFeedBacks();
    FeedBack findById(Long id, LanguageType languageType);

}
