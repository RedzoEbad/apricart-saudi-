package com.apricart.consumer.security.dto.response;

import com.apricart.consumer.security.enums.OrderType;
import com.apricart.consumer.security.enums.PaymentModeType;
import com.apricart.consumer.security.enums.PaymentStatusType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * Created on July, 2026
 *
 * @author Antigravity
 */
@Data
@Builder
public class OrderMinResponseDTO {

    private String id;
    private Long customerId;
    private Long warehouseId;
    private Double grandTotal;
    private PaymentModeType paymentMode;
    private PaymentStatusType paymentStatus;
    private OrderType orderStatus;
    private String arabicOrderStatus;
    private LocalDate deliveryDate;
    private String deliveryTime;
}
