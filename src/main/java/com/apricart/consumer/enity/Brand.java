package com.apricart.consumer.enity;

import com.apricart.consumer.security.dto.request.BrandRequestDTO;
import com.apricart.consumer.security.dto.response.BrandResponseDTO;
import lombok.*;

import javax.persistence.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Table(name = "brand")
public class Brand extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String arabicName;

    @Column
    private Boolean status;

    @Column(name = "image", columnDefinition = "TEXT") // ✅ same as in Category
    private String image;

    public static BrandResponseDTO toDTO(Brand brand) {
        return BrandResponseDTO.builder()
                .id(brand.getId())
                .name(brand.getName())
                .arabicName(brand.getArabicName())
                .image(brand.getImage())
                .status(brand.getStatus())
                .build();
    }

    public static List<BrandResponseDTO> toDTOList(List<Brand> brands) {
        return brands.stream()
                .map(Brand::toDTO)
                .filter(dto -> dto.getId() != null)
                .sorted(Comparator.comparing(BrandResponseDTO::getId))
                .collect(Collectors.toList());
    }

    public static Brand fromDTO(BrandRequestDTO brandRequestDTO) {
        return Brand.builder()
                .name(brandRequestDTO.getName())
                .image(brandRequestDTO.getImage())
                .arabicName(brandRequestDTO.getArabicName())
                .status(brandRequestDTO.getStatus())
                .build();
    }
}
