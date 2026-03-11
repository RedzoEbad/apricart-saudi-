package com.apricart.consumer.enity;

import com.apricart.consumer.security.dto.request.OrderItemRequestDTO;
import com.apricart.consumer.security.dto.response.OrderItemResponseDTO;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ORDER_ITEM")
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String arabicTitle;

    @Column
    private String quantity;

    @Column
    private String taxType;

    @Column
    private Double taxAmount;

    @Column
    private Double totalAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Orders orders;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_product_id")
    private ProductWarehouse productWarehouse;

    public static OrderItemResponseDTO toDTO(OrderItem orderItem) {
        return OrderItemResponseDTO.builder()
                .id(orderItem.getId())
                .title(orderItem.getTitle())
                .arabicTitle(orderItem.getArabicTitle())
                .quantity(orderItem.getQuantity())
                .taxAmount(orderItem.getTaxAmount())
                .totalAmount(orderItem.getTotalAmount())
                .orderId(orderItem.getOrders().getId())
                .productWarehouseId(orderItem.getProductWarehouse().getId())
                .productId(orderItem.getProductWarehouse().getProduct().getId())
                .deliveryDate(orderItem.getOrders().getDeliveryDate())
                .deliveryTime(orderItem.getOrders().getDeliveryTime())
                .build();
    }

    public static List<OrderItemResponseDTO> toDTOList(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(OrderItem::toDTO)
                .collect(Collectors.toList());
    }

    public static OrderItem fromDTO(OrderItemRequestDTO dto) {
        return OrderItem.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .arabicTitle(dto.getArabicTitle())
                .quantity(dto.getQuantity())
                .totalAmount(dto.getTotalAmount())
                .build();
    }
}

