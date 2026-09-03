package com.apricart.consumer.security.dto.dto;

import com.apricart.consumer.security.dto.response.TaxResponseDTO;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder
//@Document(indexName = "product-index")
public class ProductDetailDTO {

    @Id
    private Long id;

    private String sku;

    private String title;
    private String arabicTitle;

    private String weight;

    private String image;
    private Integer position;

    private String description;
    private String arabicDescription;

    private Boolean isActive;
    private Boolean isDeleted;
    private Boolean isFeatured;
    private Boolean isTrending;
    private Boolean isDiscounted;
    private Boolean isNewArrivals;
    private Boolean isRecommended;
    private Boolean isWishList;

    private Long taxId;
    private Long productWarehouseId;
    private Long categoryId;
    private Long subCategoryId;
    private Long warehouseId;
    private Long priceListId;

    private Long brandId;
    private String brandName;
    private String brandNameArabic;

    private Boolean inStock;
    private Integer inStockQuantity;

    private String specialRate;
    private String currentRate;
    private String rate;
    private String discountPercentage;
    private double discountedPrice;

    private TaxResponseDTO tax;
}
