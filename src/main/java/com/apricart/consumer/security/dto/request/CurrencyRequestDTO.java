package com.apricart.consumer.security.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurrencyRequestDTO {
    private Long id;
    private String currencyCode;
    private String currencySymbol;
    private String currencyPrecision;
    private String currencyFormat;
    private boolean isActive;
}