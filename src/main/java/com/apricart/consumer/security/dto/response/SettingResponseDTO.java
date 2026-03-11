package com.apricart.consumer.security.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Builder
@ToString
public class SettingResponseDTO {
    private Long id;
    private Long warehouseId;

    private String privacyPolicy;
    private String privacyPolicyArabic;

    private String splashScreen;

    private String appName;
    private String appVersion;

    private String instagramURL;
    private String twitterURL;
    private String youtubeURL;
    private String facebookURL;

    private String deliveryCharges;
    private String minOrderValue;

    private String KSANumber;
    private String UKNumber;
    private String UAENumber;
    private String PAKNumber;

    private Boolean status;

    private String ticker;

    private List<FAQResponseDTO> faqs;
    private List<FAQResponseDTO> arabicFaqs;
    private List<DeliveryTimeResponseDTO> deliveryTiming;



}
