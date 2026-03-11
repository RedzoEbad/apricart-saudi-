package com.apricart.consumer.security.dto.dto;

import lombok.*;

@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PushNotificationDTO {
    private String token;
    private String channel;
    private String topic;
    private String title;
    private String message;
}
