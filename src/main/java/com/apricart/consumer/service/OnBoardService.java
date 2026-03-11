package com.apricart.consumer.service;

import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.enity.OnBoard;
import com.apricart.consumer.security.dto.request.OnBoardRequestDTO;
import com.apricart.consumer.security.enums.LanguageType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface OnBoardService {
    OnBoard findById(Long id, LanguageType languageType);
    List<OnBoard> findAllOnBoardings(LanguageType lang);
    void addOnBoarding(OnBoardRequestDTO onBoardRequestDTO);
    void removeOnBoardById(Long id, LanguageType languageType);
    String getOnBoardingImage(Long id, LanguageType languageType);
    ResponseEntity<?> addOrUpdateOnBoardingImage(Long onBoardId, MultipartFile image, Customer customer, LanguageType lang);

}
