package com.apricart.consumer.enity;

import com.apricart.consumer.security.dto.request.PendingOrderRequestDTO;
import com.apricart.consumer.security.dto.response.PendingOrderResponseDTO;
import com.apricart.consumer.security.enums.OrderType;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Table(name = "pending_order")
public class PendingOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String orderId;

    @Column
    private Boolean status;

    public static PendingOrderResponseDTO toDTO(PendingOrder pendingOrder) {
        return PendingOrderResponseDTO.builder()
                .id(pendingOrder.getId())
                .orderId(pendingOrder.getOrderId())
                .status(pendingOrder.getStatus())
                .build();
    }

    public static List<PendingOrderResponseDTO> toDTOList(List<PendingOrder> pendingOrders) {
        return pendingOrders.stream()
                .map(PendingOrder::toDTO)
                .collect(Collectors.toList());
    }

    public static PendingOrder fromDTO(PendingOrderRequestDTO dto) {
        return PendingOrder.builder()
                .orderId(dto.getOrderId())
                .status(dto.getStatus())
                .build();
    }
}
