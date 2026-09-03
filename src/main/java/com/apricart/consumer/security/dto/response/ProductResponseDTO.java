package com.apricart.consumer.security.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductResponseDTO {
    private Long id;
    private String title;
    private String arabicTitle;
    private String weight;
    private String image;
    private String description;
    private String arabicDescription;
    private String sku;
    private Boolean isActive;
    private Boolean isDeleted;
    private Boolean isFeatured;
    private Boolean isTrending;
    private Boolean isDiscounted;
    private Boolean isNewArrivals;
    private Boolean isRecommended;
    private Integer position;
    private Long categoryId;
    private Long subCategoryId;
    private Long brandId;
    private String brandName;
    private String brandNameArabic;

}
