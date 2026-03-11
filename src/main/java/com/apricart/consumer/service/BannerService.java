package com.apricart.consumer.service;

import com.apricart.consumer.enity.Banner;
import com.apricart.consumer.enity.Customer;
import com.apricart.consumer.security.dto.request.BannerRequestDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.security.enums.LevelType;
import com.apricart.consumer.security.enums.PositionType;
import com.google.protobuf.ServiceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BannerService {
    List<Banner> getAllBanners(LanguageType lang);
    Banner findById(Long id, LanguageType languageType);
    List<Banner> findByPosition(PositionType positionType);
    List<Banner> findByLevel(LevelType levelType);
    List<Banner> getActiveBanners(LanguageType lang);
    String getBannerImage(Long id, LanguageType languageType);
    void addBanner(BannerRequestDTO brandRequestDTO) throws ServiceException;
    Banner updateBanner(BannerRequestDTO brandRequestDTO, LanguageType languageType);
    void deleteBanner(Long id, LanguageType languageType);
    ResponseEntity<?> addOrUpdateBannerImage(Long brandId, MultipartFile image, Customer customer, LanguageType lang);
}
