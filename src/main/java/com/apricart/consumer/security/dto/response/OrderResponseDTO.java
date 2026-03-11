package com.apricart.consumer.security.dto.response;

import com.apricart.consumer.security.enums.DiscountType;
import com.apricart.consumer.security.enums.OrderType;
import com.apricart.consumer.security.enums.PaymentModeType;
import com.apricart.consumer.security.enums.PaymentStatusType;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@ToString
public class OrderResponseDTO {

    private String id;

    private String notes;
    private Boolean status;

    private Long customerId;
    private Long warehouseId;

    private Double netTotal;
    private Double grandTotal;

    private Double deliveryCharges;
    private Double minOrderValue;

    private Double totalTax;
    private Double totalDiscount;

    private String couponCode;
    private DiscountType discountType;

    private String zohoSalesOrderNum;

    private PaymentModeType paymentMode;
    private PaymentStatusType paymentStatus;
    private OrderType orderStatus;
    private String arabicOrderStatus;

    private LocalDate deliveryDate;
    private String deliveryTime;

    private LocalDateTime createdDateTime;
    private LocalDateTime updateDateTime;

    private CurrencyResponseDTO currency;
    private CustomerAddressResponseDTO customerAddress;
    private List<OrderItemResponseDTO> orderItems;
}