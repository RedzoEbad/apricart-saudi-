package com.apricart.consumer.security.dto.dto;

import lombok.*;

@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDTO {
    private String sendTo;
    private String type;
    private String value;
    private String title;
    private String message;
    private String city;
}
