package com.apricart.consumer.enity;

import com.apricart.consumer.mapper.OrderMapper;
import com.apricart.consumer.security.dto.request.OrderRequestDTO;
import com.apricart.consumer.security.dto.response.OrderMinResponseDTO;
import com.apricart.consumer.security.dto.response.OrderResponseDTO;
import com.apricart.consumer.security.enums.*;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ORDERS")
public class Orders extends BaseEntity {
    
    @Id
    private String id;

    @Column
    private String zohoSalesOrderNum;

    @Column
    private String shippingCharge;

    @Column
    private Double totalTax;

    @Column
    private Double totalDiscount;

    @Column
    private Double subTotal;

    @Column
    private Double grandTotal;

    @Column
    private Double deliveryCharges;

    @Column
    private Double minOrderValue;

    @Enumerated(EnumType.STRING)
    private PaymentModeType paymentMode;

    @Column
    private String notes;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    @Column
    private String couponCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderType orderStatus;

    @Enumerated(EnumType.STRING)
    private PaymentStatusType paymentStatus;

    @Column
    private Boolean status;

    @Column
    private String orderTypeArabic;

    @Column
    private LocalDate deliveryDate;

    @Column
    private String deliveryTime;

    @Column
    private Long warehouseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_address_id")
    private CustomerAddress customerAddress;

    @OneToMany(mappedBy = "orders")
    private List<OrderItem> orderItems;

    public static OrderMinResponseDTO toMinDTO(Orders orders) {
        return OrderMinResponseDTO.builder()
                .id(orders.getId())
                .customerId(orders.getCustomer() != null ? orders.getCustomer().getId() : null)
                .warehouseId(orders.getWarehouseId())
                .grandTotal(orders.getGrandTotal())
                .paymentMode(orders.getPaymentMode())
                .paymentStatus(orders.getPaymentStatus())
                .orderStatus(orders.getOrderStatus())
                .arabicOrderStatus(orders.getOrderTypeArabic())
                .deliveryDate(orders.getDeliveryDate())
                .deliveryTime(orders.getDeliveryTime())
                .build();
    }

    public static OrderResponseDTO toDTO(Orders orders, OrderMapper orderMapper, LanguageType languageType) {
        return OrderResponseDTO.builder()
                .id(orders.getId())
                .notes(orders.getNotes())
                .couponCode(orders.getCouponCode())
                .grandTotal(orders.getGrandTotal())
                .netTotal(orders.getSubTotal())
                .paymentMode(orders.getPaymentMode())
                .discountType(orders.getDiscountType())
                .minOrderValue(orders.getMinOrderValue())
                .deliveryCharges(orders.getDeliveryCharges())
                .paymentStatus(orders.getPaymentStatus())
                .zohoSalesOrderNum(orders.getZohoSalesOrderNum())
                .totalTax(orders.getTotalTax())
                .totalDiscount(orders.getTotalDiscount())
                .deliveryDate(orders.getDeliveryDate())
                .deliveryTime(orders.getDeliveryTime())
                .orderStatus(orders.getOrderStatus())
                .arabicOrderStatus(orders.getOrderTypeArabic())
                .orderItems(orderMapper.toOrderItemList(orders.getOrderItems(), languageType))
                .status(orders.getStatus())
                .customerId(orders.getCustomer().getId())
                .currency(Currency.toDTO(orders.getCurrency()))
                .customerAddress(CustomerAddress.toDTO(orders.getCustomerAddress()))
                .warehouseId(orders.getWarehouseId())
                .build();
    }

    public static List<OrderResponseDTO> toDTOList(List<Orders> orders, OrderMapper orderMapper, LanguageType languageType) {
        return orders.stream()
                .map(order -> toDTO(order, orderMapper, languageType))
                .collect(Collectors.toList());
    }

    public static Orders fromDTO(OrderRequestDTO dto) {
        return Orders.builder()
                .id(dto.getId())
                .notes(dto.getNotes())
                .couponCode(dto.getCouponCode())
                .grandTotal(dto.getGrandTotal())
                .subTotal(dto.getSubTotal())
                .paymentMode(dto.getPaymentMode())
                .discountType(dto.getDiscountType())
                .paymentStatus(dto.getPaymentStatus())
                .totalTax(dto.getTotalTax())
                .totalDiscount(dto.getTotalDiscount())
                .minOrderValue(dto.getMinOrderValue())
                .deliveryCharges(dto.getDeliveryCharges())
                .orderStatus(dto.getOrderStatus())
                .deliveryDate(dto.getDeliveryDate())
                .deliveryTime(dto.getDeliveryTime())
                .warehouseId(dto.getWarehouseId())
                .status(dto.getStatus())
                .build();
    }
}

