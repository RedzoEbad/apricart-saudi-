package com.apricart.consumer.security.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaxRequestDTO {
    private Long id;
    private String taxName;
    private double taxPercentage;
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
