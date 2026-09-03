package com.apricart.consumer.security.dto.response;

import com.apricart.consumer.security.enums.LevelType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDTO {
    private Long id;
    private String name;
    private String arabicName;
    private LevelType level;
    private Integer position;
    private Boolean status;
    private Boolean isDeleted;
    private Boolean isDiscountedCategory;
    private String image;
    private List<SubCategoryResponseDTO> subCategories;
}