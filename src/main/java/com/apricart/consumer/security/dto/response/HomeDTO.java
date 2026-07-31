package com.apricart.consumer.security.dto.response;

import com.apricart.consumer.security.dto.dto.ProductDetailDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class HomeDTO {

    private List<CategoryResponseDTO> categories;
    private List<ProductDetailDTO> newArrivals;
    private List<ProductDetailDTO> trending;
    private List<ProductDetailDTO> recommended;
    private List<BrandResponseDTO> brands;
    private List<BannerResponseDTO> banners;

}