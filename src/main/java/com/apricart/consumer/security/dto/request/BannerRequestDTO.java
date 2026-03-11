package com.apricart.consumer.security.dto.request;

import com.apricart.consumer.security.enums.LevelType;
import com.apricart.consumer.security.enums.PositionType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BannerRequestDTO {
    private Long id;
    private String name;
    private String arabicName;
    private Boolean status;
    private String image;
    private LevelType level;
    private PositionType position;

}
