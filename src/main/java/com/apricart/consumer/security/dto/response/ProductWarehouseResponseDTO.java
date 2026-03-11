package com.apricart.consumer.security.dto.response;

import com.apricart.consumer.security.enums.PositionType;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@Builder
@ToString
public class ProductWarehouseResponseDTO implements Serializable {
    private Long id;
    private Boolean isActive;
    private Boolean inStock;
    private String specialRate;
    private String currentRate;
    private String discountPercentage;
    private String rate;
    private Integer quantityInStock;
    private Long categoryId;
    private Long subCategoryId;
    private Long warehouseId;
    private Long priceListId;
    private Long taxId;
    private ProductResponseDTO product;

}
