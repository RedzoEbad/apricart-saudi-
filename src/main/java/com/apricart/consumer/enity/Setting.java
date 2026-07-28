package com.apricart.consumer.enity;

import com.apricart.consumer.security.dto.request.SettingRequestDTO;
import com.apricart.consumer.security.dto.response.SettingResponseDTO;
import lombok.*;

import javax.persistence.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Setting")
public class Setting extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String privacyPolicy;

    @Column(columnDefinition = "TEXT")
    private String privacyPolicyArabic;

    @Column(columnDefinition = "TEXT")
    private String termsAndConditions;

    @Column(columnDefinition = "TEXT")
    private String termsAndConditionsArabic;

    @Column
    private String splashScreen;

    @Column
    private String appName;

    @Column
    private String appVersion;

    @Column
    private String instagramURL;

    @Column
    private String twitterURL;

    @Column
    private String youtubeURL;

    @Column
    private String facebookURL;

    @Column
    private String deliveryCharges;

    @Column
    private String minOrderValue;

    @Column
    private String KSANumber;

    @Column
    private String UKNumber;

    @Column
    private String UAENumber;

    @Column
    private String PAKNumber;

    @Column
    private Long warehouseId;

    @Column
    private Boolean status;

    @Column
    private String ticker;

    @OneToMany(mappedBy = "setting", cascade = CascadeType.ALL)
    private List<FAQ> faqs;

    @OneToMany(mappedBy = "setting", cascade = CascadeType.ALL)
    private List<FAQ> arabicFaqs;

    @OneToMany(mappedBy = "setting", cascade = CascadeType.ALL)
    private List<DeliveryTime> deliveryTiming;

    public static SettingResponseDTO toDTO(Setting setting) {
        return SettingResponseDTO.builder()
                .id(setting.getId())
                .warehouseId(setting.getWarehouseId())
                .privacyPolicy(setting.getPrivacyPolicy())
                .privacyPolicyArabic(setting.getPrivacyPolicyArabic())
                .termsAndConditions(setting.getTermsAndConditions())
                .termsAndConditionsArabic(setting.getTermsAndConditionsArabic())
                .splashScreen(setting.getSplashScreen())
                .appName(setting.getAppName())
                .appVersion(setting.getAppVersion())
                .minOrderValue(setting.getMinOrderValue())
                .deliveryCharges(setting.getDeliveryCharges())
                .instagramURL(setting.getInstagramURL())
                .facebookURL(setting.getFacebookURL())
                .twitterURL(setting.getTwitterURL())
                .youtubeURL(setting.getYoutubeURL())
                .KSANumber(setting.getKSANumber())
                .UAENumber(setting.getUAENumber())
                .UKNumber(setting.getUKNumber())
                .PAKNumber(setting.getPAKNumber())
                .ticker(setting.getTicker())
                .status(setting.getStatus())
//                .faqs(FAQ.toDTOList(setting.getFaqs()))
                .deliveryTiming(DeliveryTime.toDTOList(setting.getDeliveryTiming()))
                .build();
    }

    public static List<SettingResponseDTO> toDTOList(List<Setting> settings) {
        return settings.stream()
                .sorted(Comparator.comparingLong(Setting::getId))
                .filter(Setting::getStatus)
                .map(Setting::toDTO)
                .collect(Collectors.toList());
    }
    public static Setting fromDTO(SettingRequestDTO dto) {
        return Setting.builder()
                .warehouseId(dto.getWarehouseId())
                .privacyPolicy(dto.getPrivacyPolicy())
                .privacyPolicyArabic(dto.getPrivacyPolicyArabic())
                .termsAndConditions(dto.getTermsAndConditions())
                .termsAndConditionsArabic(dto.getTermsAndConditionsArabic())
                .splashScreen(dto.getSplashScreen())
                .appName(dto.getAppName())
                .appVersion(dto.getAppVersion())
                .minOrderValue(dto.getMinOrderValue())
                .deliveryCharges(dto.getDeliveryCharges())
                .instagramURL(dto.getInstagramURL())
                .facebookURL(dto.getFacebookURL())
                .twitterURL(dto.getTwitterURL())
                .youtubeURL(dto.getYoutubeURL())
                .KSANumber(dto.getKSANumber())
                .UAENumber(dto.getUAENumber())
                .UKNumber(dto.getUKNumber())
                .PAKNumber(dto.getPAKNumber())
                .ticker(dto.getTicker())
                .status(dto.getStatus())
                .build();
    }
}
