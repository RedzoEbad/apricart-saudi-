package com.apricart.consumer.security.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class FAQRequestDTO {
    private Long id;
    private String question;
    private String answer;
    private Boolean status;
}
