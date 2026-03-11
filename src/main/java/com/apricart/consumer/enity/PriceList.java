package com.apricart.consumer.enity;

import com.apricart.consumer.security.dto.request.PriceListRequestDTO;
import com.apricart.consumer.security.dto.response.CityResponseDTO;
import com.apricart.consumer.security.dto.response.PriceListResponseDTO;
import com.apricart.consumer.security.enums.PriceBookType;
import com.apricart.consumer.security.enums.RoundingType;
import com.apricart.consumer.security.enums.SalesOrPurchaseType;
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
@AllArgsConstructor
@Table(name = "PRICE_LIST")
public class PriceList extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String arabicName;

    @Column
    private Boolean isIncrease;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    private PriceBookType priceBookType;

    @Enumerated(EnumType.STRING)
    private RoundingType roundingType;

    @Enumerated(EnumType.STRING)
    private SalesOrPurchaseType salesOrPurchaseType;

    @Column
    private boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    private Currency currency;


    public static PriceListResponseDTO toDTO(PriceList priceList) {
        return PriceListResponseDTO.builder()
                .id(priceList.getId())
                .name(priceList.getName())
                .arabicName(priceList.getArabicName())
                .isIncrease(priceList.getIsIncrease())
                .description(priceList.getDescription())
                .priceBookType(priceList.getPriceBookType())
                .roundingType(priceList.getRoundingType())
                .currencyId(priceList.getCurrency().getId())
                .salesOrPurchaseType(priceList.getSalesOrPurchaseType())
                .isActive(priceList.isActive())
                .build();
    }
    public static List<PriceListResponseDTO> toDTOList(List<PriceList> priceLists) {
        return priceLists.stream()
                .map(PriceList::toDTO)
                .filter(dto -> dto.getId() != null)
                .sorted(Comparator.comparing(PriceListResponseDTO::getId))
                .collect(Collectors.toList());
    }
    public static PriceList fromDTO(PriceListRequestDTO dto) {
        return PriceList.builder()
                .id(dto.getId())
                .name(dto.getName())
                .arabicName(dto.getArabicName())
                .isIncrease(dto.getIsIncrease())
                .description(dto.getDescription())
                .priceBookType(dto.getPriceBookType())
                .roundingType(dto.getRoundingType())
                .salesOrPurchaseType(dto.getSalesOrPurchaseType())
                .isActive(dto.isActive())
                .build();
    }
}
