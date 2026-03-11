package com.apricart.consumer.security.dto.request;


import com.apricart.consumer.security.enums.PositionType;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class ProductWarehouseRequestDTO {
    private Long id;
    private Boolean isActive;
    private Boolean inStock;
    private String specialRate;
    private String currentRate;
    private String rate;
    private Integer quantityInStock;
    private PositionType position;
    private Long productId;
    private Long warehouseId;
    private Long priceListId;
    private Long taxId;
    private Long categoryId;
    private Long subCategoryId;

}
