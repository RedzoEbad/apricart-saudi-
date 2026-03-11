package com.apricart.consumer.enity;

import com.apricart.consumer.security.dto.dto.NotificationDTO;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "NOTIFICATION")
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String message;
    @Column
    private String value;

    @Column
    private String type;

    private Long customerId;
    public static Notification fromDTO(NotificationDTO dto) {
        return Notification.builder()
                .title(dto.getTitle())
                .message(dto.getMessage())
                .value(dto.getValue())
                .type(dto.getType())
                .build();
    }
}
