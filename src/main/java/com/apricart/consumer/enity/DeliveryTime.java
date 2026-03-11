package com.apricart.consumer.enity;

import com.apricart.consumer.security.dto.request.DeliveryTimeRequestDTO;
import com.apricart.consumer.security.dto.response.DeliveryTimeResponseDTO;
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
@Table(name = "DeliveryTime")
public class DeliveryTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String deliveryTime;

    @Column
    private Boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "setting_id")
    private Setting setting;

    public static DeliveryTimeResponseDTO toDTO(DeliveryTime deliveryTime) {
        return DeliveryTimeResponseDTO.builder()
                .id(deliveryTime.getId())
                .deliveryTime(deliveryTime.getDeliveryTime())
                .status(deliveryTime.getStatus())
                .build();
    }

    public static List<DeliveryTimeResponseDTO> toDTOList(List<DeliveryTime> deliveryTimes) {
        return deliveryTimes.stream()
                .sorted(Comparator.comparingLong(DeliveryTime::getId))
                .filter(DeliveryTime::getStatus)
                .map(DeliveryTime::toDTO)
                .collect(Collectors.toList());
    }
    public static DeliveryTime fromDTO(DeliveryTimeRequestDTO requestDTO) {
        return DeliveryTime.builder()
                .deliveryTime(requestDTO.getDeliveryTime())
                .status(requestDTO.getStatus())
                .build();
    }
}
