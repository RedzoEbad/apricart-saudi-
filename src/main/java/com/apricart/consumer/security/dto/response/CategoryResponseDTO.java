package com.apricart.consumer.security.dto.response;

import com.apricart.consumer.security.enums.LevelType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryResponseDTO {
    private Long id;
    private String name;
    private String arabicName;
    private LevelType level;
    private Integer position;
    private Boolean status;
    private Boolean isDiscountedCategory;
    private String image;
}