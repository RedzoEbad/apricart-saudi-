package com.apricart.consumer.security.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class ProductRequestDTO {
    private Long id;
    private String title;
    private String arabicTitle;
    private String image;
    private String weight;
    private String description;
    private String arabicDescription;
    private String sku;
    private Boolean isActive;
    private Boolean isFeatured;
    private Boolean isTrending;
    private Boolean isDiscounted;
    private Boolean isNewArrivals;
    private Boolean isRecommended;
    private Integer position;
    private Long categoryId;
    private Long subCategoryId;
    private Long brandId;
    private Long zohoId;
}
