package com.apricart.consumer.security.dto.response;

import com.apricart.consumer.security.enums.StatusType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeedBackResponseDTO {

    private Long id;
    private String name;
    private String phoneNumber;
    private String email;
    private String message;
    private StatusType feedbackStatus;
}
