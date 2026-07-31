package com.apricart.consumer.security.dto.response;

import com.apricart.consumer.security.dto.dto.ProductDetailDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDetailsDTO {

    private List<SubCategoryResponseDTO> subCategories;
    private List<ProductDetailDTO> products;

}
