package com.apricart.consumer.security.dto.response;

import com.apricart.consumer.security.enums.LevelType;
import com.apricart.consumer.security.enums.PositionType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubCategoryResponseDTO {
    private Long id;
    private String name;
    private String arabicName;
    private LevelType level;
    private Integer position;
    private Boolean status;
    private String image;
    private Long categoryId;
}

