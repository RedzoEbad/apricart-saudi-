package com.apricart.consumer.security.dto.response;

import com.apricart.consumer.security.dto.dto.ProductDetailDTO;
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
public class SubCategoryResponseDTO {
    private Long id;
    private String name;
    private String arabicName;
    private LevelType level;
    private Integer position;
    private Boolean status;
    private String image;
    private Long categoryId;
    private List<ProductDetailDTO> products;
}

