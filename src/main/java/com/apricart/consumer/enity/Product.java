package com.apricart.consumer.enity;

import com.apricart.consumer.security.dto.request.ProductRequestDTO;
import com.apricart.consumer.security.dto.response.ProductResponseDTO;
import com.apricart.consumer.utils.annotation.SKUFormat;
import lombok.*;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PRODUCT")
@Indexed
public class Product extends BaseEntity {

    @SortableField
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Field(index= org.hibernate.search.annotations.Index.YES, analyze= Analyze.YES, store= Store.NO, termVector = TermVector.YES)
    @Column
    private String title;

    @Field(index= org.hibernate.search.annotations.Index.YES, analyze= Analyze.YES, store= Store.NO, termVector = TermVector.YES)
    @Column
    private String arabicTitle;

    @Column(name = "image", columnDefinition = "TEXT")
    private String image;

    @Field(index= org.hibernate.search.annotations.Index.YES, analyze= Analyze.YES, store= Store.NO, termVector = TermVector.YES)
    @Column(length = 24, unique = true)
    @SKUFormat
    private String sku;

    @Field(index= org.hibernate.search.annotations.Index.YES, analyze= Analyze.YES, store= Store.NO, termVector = TermVector.YES)
    @Column
    private String description;

    @Field(index= org.hibernate.search.annotations.Index.YES, analyze= Analyze.YES, store= Store.NO, termVector = TermVector.YES)
    @Column
    private String arabicDescription;

    @Column
    private String weight;

    @Column
    private Boolean isActive;

    @Column
    private Boolean isFeatured;

    @Column
    private Boolean isTrending;

    @Column
    private Boolean isDiscounted;

    @Column
    private Boolean isNewArrivals;

    @Column
    private Boolean isRecommended;

    @Column
    private Integer position;

    @Column
    private Long zohoId;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    private static ProductResponseDTO.ProductResponseDTOBuilder buildDTO(Product product, ProductResponseDTO.ProductResponseDTOBuilder builder) {
        return builder
                .id(product.getId())
                .arabicTitle(product.getArabicTitle())
                .arabicDescription(product.getArabicDescription())
                .description(product.getDescription())
                .weight(product.getWeight())
                .sku(product.getSku())
                .title(product.getTitle())
                .isDiscounted(product.getIsDiscounted())
                .isFeatured(product.getIsFeatured())
                .isNewArrivals(product.getIsNewArrivals())
                .isRecommended(product.getIsRecommended())
                .isTrending(product.getIsTrending())
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .subCategoryId(product.getSubCategory() != null ? product.getSubCategory().getId() : null)
                .brandId(product.getBrand() != null ? product.getBrand().getId() : null)
                .brandName(product.getBrand() != null ? product.getBrand().getName() : null)
                .brandNameArabic(product.getBrand() != null ? product.getBrand().getArabicName() : null)
                .position(product.getPosition())
                .image(product.getImage())
                .isActive(product.getIsActive());
    }

    public static ProductResponseDTO toDTO(Product product) {
        return buildDTO(product, ProductResponseDTO.builder()).build();
    }

    public static List<ProductResponseDTO> toDTOList(List<Product> products) {
        return products.stream()
                .map(Product::toDTO)
                .filter(dto -> dto.getPosition() != null)
                .sorted(Comparator.comparing(ProductResponseDTO::getPosition))
                .collect(Collectors.toList());
    }

    public static Product fromDTO(ProductRequestDTO dto) {
        return Product.builder()
                .id(dto.getId())
                .arabicTitle(dto.getArabicTitle())
                .description(dto.getDescription())
                .arabicDescription(dto.getArabicDescription())
                .sku(dto.getSku())
                .weight(dto.getWeight())
                .title(dto.getTitle())
                .isDiscounted(dto.getIsDiscounted())
                .isRecommended(dto.getIsRecommended())
                .isFeatured(dto.getIsFeatured())
                .isNewArrivals(dto.getIsNewArrivals())
                .isTrending(dto.getIsTrending())
                .position(dto.getPosition())
                .image(dto.getImage())
                .isActive(dto.getIsActive())
                .zohoId(dto.getZohoId())
                .build();
    }
}
