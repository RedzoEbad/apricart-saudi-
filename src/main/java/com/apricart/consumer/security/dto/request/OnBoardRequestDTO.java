package com.apricart.consumer.security.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OnBoardRequestDTO {
    private Long id;
    private String title;
    private String arabicTitle;
    private String description;
    private String arabicDescription;
    private String image;
}
