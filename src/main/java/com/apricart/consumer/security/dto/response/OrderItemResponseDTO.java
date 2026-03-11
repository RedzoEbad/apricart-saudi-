package com.apricart.consumer.security.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItemResponseDTO {

    private Long id;
    private String orderId;
    private Long productId;
    private Long productWarehouseId;

    private String title;
    private String arabicTitle;

    private String quantity;
    private String weight;
    private String image;

    private Double taxAmount;
    private String taxType;
    private Double totalAmount;

    private LocalDate deliveryDate;
    private String deliveryTime;


}
