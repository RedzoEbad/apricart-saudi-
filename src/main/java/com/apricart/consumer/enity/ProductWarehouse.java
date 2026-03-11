package com.apricart.consumer.enity;

import com.apricart.consumer.mapper.ProductMapper;
import com.apricart.consumer.security.dto.request.ProductWarehouseRequestDTO;
import com.apricart.consumer.security.dto.response.ProductWarehouseResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import lombok.*;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.SortableField;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PRODUCT_WAREHOUSE")
@Indexed
public class ProductWarehouse {
    @SortableField
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Boolean isActive;

    @Column
    private Boolean inStock;

    @Column
    private String specialRate;

    @Column
    private String currentRate;

    @Column
    private String rate;

    @Column
    private String discountPercentage;

    @Column
    private Integer quantityInStock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @IndexedEmbedded
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    @IndexedEmbedded(includePaths = {"id"})
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pricelist_id")
    private PriceList priceList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tax_id")
    private Tax tax;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;

    public static ProductWarehouseResponseDTO toDTO(ProductWarehouse productWarehouse, ProductMapper productMapper, LanguageType languageType) {
        return ProductWarehouseResponseDTO.builder()
                .id(productWarehouse.getId())
                .currentRate(productWarehouse.getCurrentRate())
                .inStock(productWarehouse.getInStock())
                .product(productMapper.toProductDTO(productWarehouse, languageType))
                .rate(productWarehouse.getRate())
                .taxId(productWarehouse.getTax().getId())
                .quantityInStock(productWarehouse.getQuantityInStock())
                .warehouseId(productWarehouse.getWarehouse().getId())
                .specialRate(productWarehouse.getSpecialRate())
                .priceListId(productWarehouse.getPriceList().getId())
                .categoryId(productWarehouse.getCategory().getId())
                .discountPercentage(productWarehouse.getDiscountPercentage())
                .subCategoryId(productWarehouse.getSubCategory().getId())
                .isActive(productWarehouse.getIsActive())
                .build();
    }
    public static List<ProductWarehouseResponseDTO> toDTOList(List<ProductWarehouse> productWarehouse, ProductMapper productMapper, LanguageType languageType) {
        return productWarehouse.stream()
                .map(pw -> toDTO(pw,productMapper, languageType))
                .collect(Collectors.toList());
    }
    public static ProductWarehouse fromDTO(ProductWarehouseRequestDTO dto) {
        return ProductWarehouse.builder()
                .id(dto.getId())
                .currentRate(dto.getCurrentRate())
                .inStock(dto.getInStock())
                .rate(dto.getRate())
                .quantityInStock(dto.getQuantityInStock())
                .specialRate(dto.getSpecialRate())
                .isActive(dto.getIsActive())
                .build();
    }
}
