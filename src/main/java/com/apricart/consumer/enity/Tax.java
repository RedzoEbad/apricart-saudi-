package com.apricart.consumer.enity;

import com.apricart.consumer.security.dto.request.TaxRequestDTO;
import com.apricart.consumer.security.dto.response.TaxResponseDTO;
import lombok.*;

import javax.persistence.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Tax")
public class Tax {

//    @Id
//    private String taxId;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String taxName;

    @Column
    private double taxPercentage;

    @Column
    private String taxType;

    @Column
    private String taxFactor;

    @Column
    private String tdsPayableAccountId;

    @Column
    private String taxAuthorityId;

    @Column
    private String taxAuthorityName;

    @Column
    private boolean isValueAdded;

    @Column(name = "tax_specific_type")
    private String taxSpecificType;

    @Column
    private String countryCode;

    @Column
    private long purchaseTaxExpenseAccountId;
    public static TaxResponseDTO toDTO(Tax tax) {
        return TaxResponseDTO.builder()
                .id(tax.getId())
                .countryCode(tax.getCountryCode())
                .isValueAdded(tax.isValueAdded())
                .taxAuthorityId(tax.getTaxAuthorityId())
                .taxName(tax.getTaxName())
                .taxFactor(tax.getTaxFactor())
                .taxPercentage(String.valueOf(tax.getTaxPercentage()))
                .taxAuthorityName(tax.getTaxAuthorityName())
                .taxType(tax.getTaxType())
                .purchaseTaxExpenseAccountId(tax.getPurchaseTaxExpenseAccountId())
                .taxSpecificType(tax.getTaxSpecificType())
                .tdsPayableAccountId(tax.getTdsPayableAccountId())
                .build();
    }
    public static List<TaxResponseDTO> toDTOList(List<Tax> taxes) {
        return taxes.stream()
                .map(Tax::toDTO)
                .filter(dto -> dto.getId() != null)
                .sorted(Comparator.comparing(TaxResponseDTO::getId))
                .collect(Collectors.toList());
    }
    public static Tax fromDTO(TaxRequestDTO dto) {
        return Tax.builder()
                .id(dto.getId())
                .countryCode(dto.getCountryCode())
                .isValueAdded(dto.isValueAdded())
                .taxAuthorityId(dto.getTaxAuthorityId())
                .taxName(dto.getTaxName())
                .taxFactor(dto.getTaxFactor())
                .taxPercentage(dto.getTaxPercentage())
                .taxAuthorityName(dto.getTaxAuthorityName())
                .taxType(dto.getTaxType())
                .purchaseTaxExpenseAccountId(dto.getPurchaseTaxExpenseAccountId())
                .taxSpecificType(dto.getTaxSpecificType())
                .tdsPayableAccountId(dto.getTdsPayableAccountId())
                .build();
    }

}
