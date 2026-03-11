package com.apricart.consumer.enity;

import com.apricart.consumer.security.dto.dto.MissingProductDTO;
import com.apricart.consumer.security.dto.request.MissingProductRequestDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.security.enums.StatusType;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MISSING_PRODUCT")
public class MissingProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private Integer quantity;

    @Lob
    private String image;

    @Enumerated(EnumType.STRING)
    private StatusType productStatus;

    @Enumerated(value = EnumType.STRING)
    private LanguageType language;

    @Column
    private Long customerId;
    public static MissingProductDTO toDTO(MissingProduct missingProduct) {
        return MissingProductDTO.builder()
                .id(missingProduct.getId())
                .name(missingProduct.getName())
                .description(missingProduct.getDescription())
                .quantity(missingProduct.getQuantity())
                .productStatus(missingProduct.getProductStatus())
                .language(missingProduct.getLanguage())
                .customerId(missingProduct.getCustomerId())
                .image(missingProduct.getImage())
                .build();
    }

    public static MissingProduct fromDTO(MissingProductRequestDTO dto) {
        return MissingProduct.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .language(dto.getLanguage())
                .quantity(dto.getQuantity())
                .customerId(dto.getCustomerId())
                .build();
    }

}
