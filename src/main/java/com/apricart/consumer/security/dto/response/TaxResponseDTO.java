package com.apricart.consumer.security.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class TaxResponseDTO {
    private Long id;
    private String taxName;
    private String taxPercentage;
    private double taxAmount;
    private String taxType;
    private String taxFactor;
    private String tdsPayableAccountId;
    private String taxAuthorityId;
    private String taxAuthorityName;
    private boolean isValueAdded;
    private String taxSpecificType;
    private String countryCode;
    private long purchaseTaxExpenseAccountId;

}

