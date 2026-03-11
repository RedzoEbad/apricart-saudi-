package com.apricart.consumer.security.dto.response;

import com.apricart.consumer.security.enums.LanguageType;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class FAQResponseDTO {
    private Long id;
    private String question;
    private String answer;
    private LanguageType languageType;
    private Boolean status;
}
