package com.apricart.consumer.security.dto.request;

import com.apricart.consumer.security.enums.DiscountType;
import com.apricart.consumer.security.enums.OrderType;
import com.apricart.consumer.security.enums.PaymentModeType;
import com.apricart.consumer.security.enums.PaymentStatusType;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@ToString
public class OrderRequestDTO {

    private String id;
    private Double totalTax;
    private Double totalDiscount;

    private Double minOrderValue;
    private Double deliveryCharges;

    private Double subTotal;
    private Double grandTotal;

    private String notes;

    private DiscountType discountType;
    private String couponCode;

    private PaymentModeType paymentMode;
    private OrderType orderStatus;
    private PaymentStatusType paymentStatus;
    private Boolean status;

    private LocalDate deliveryDate;
    private String deliveryTime;

    private Long currencyId;
    private Long warehouseId;
    private Long customerAddressId;

    private List<OrderItemRequestDTO> orderItems;
}
