package com.apricart.consumer.security.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class SettingRequestDTO {
    private Long id;
    private Long warehouseId;

    private String privacyPolicy;
    private String privacyPolicyArabic;

    private String termsAndConditions;
    private String termsAndConditionsArabic;
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
}
